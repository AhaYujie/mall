package online.ahayujie.mall.admin.pms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.bean.model.MysqlExplain;
import online.ahayujie.mall.admin.config.RabbitmqConfig;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.bean.model.*;
import online.ahayujie.mall.admin.pms.exception.*;
import online.ahayujie.mall.admin.pms.mapper.ProductImageMapper;
import online.ahayujie.mall.admin.pms.mapper.ProductMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuMapper;
import online.ahayujie.mall.admin.pms.publisher.ProductPublisher;
import online.ahayujie.mall.admin.pms.service.*;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private SkuService skuService;
    private BrandService brandService;
    private ProductPublisher productPublisher;
    private ProductParamService productParamService;
    private ProductCategoryService productCategoryService;
    private ProductVerifyLogService productVerifyLogService;
    private ProductSpecificationService productSpecificationService;

    private final SkuMapper skuMapper;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;

    public ProductServiceImpl(SkuMapper skuMapper, ObjectMapper objectMapper, ProductMapper productMapper,
                              ProductImageMapper productImageMapper) {
        this.skuMapper = skuMapper;
        this.objectMapper = objectMapper;
        this.productMapper = productMapper;
        this.productImageMapper = productImageMapper;
    }

    @Override
    public void create(CreateProductParam param) throws IllegalProductException, IllegalProductSpecificationException,
            IllegalProductParamException, IllegalSkuException, IllegalProductCategoryException, IllegalBrandException {
        // 检查商品信息合法性
        Product product = new Product();
        BeanUtils.copyProperties(param.getProduct(), product);
        validateProduct(product);
        // 检查商品规格合法性
        validateSpecifications(param.getSpecifications());
        // 检查商品参数合法性
        validateProductParams(param.getParams());
        // 检查sku合法性
        validateSkus(param.getSkus(), param.getSpecifications());
        // 保存商品信息
        Product saveProduct = saveCreateProduct(param.getProduct());
        // 保存商品参数信息
        saveCreateProductParam(param.getParams(), saveProduct.getId());
        // 保存商品规格信息
        List<ProductSpecification> specifications = saveCreateSpecifications(saveProduct.getId(), param.getSpecifications());
        List<List<ProductSpecificationValue>> specificationValueLists = saveCreateSpecificationValues(specifications,
                param.getSpecifications());
        // 保存商品sku信息
        saveCreateSkus(param.getSkus(), specifications, specificationValueLists, saveProduct.getId());
        // 发送消息到消息队列
        productPublisher.publishCreateMsg(new ProductCreateMsgDTO(saveProduct.getId()));
    }

    @Override
    public void validateProduct(Product product) throws IllegalProductException, IllegalProductCategoryException,
            IllegalBrandException {
        Long brandId = product.getBrandId();
        if (brandId != null && brandService.getById(brandId) == null) {
            throw new IllegalBrandException("品牌不存在");
        }
        Long productCategoryId = product.getProductCategoryId();
        if (productCategoryId != null && productCategoryService.getById(productCategoryId) == null) {
            throw new IllegalProductCategoryException("商品分类不存在");
        }
        BigDecimal price = product.getPrice();
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalProductException("价格小于0: " + price);
        }
        BigDecimal originalPrice = product.getOriginalPrice();
        if (originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalProductException("市场价小于0: " + originalPrice);
        }
        Integer sale = product.getSale();
        if (sale != null && sale.compareTo(0) < 0) {
            throw new IllegalProductException("销量小于0: " + sale);
        }
        Integer stock = product.getStock();
        if (stock != null && stock.compareTo(0) < 0) {
            throw new IllegalProductException("库存小于0: " + stock);
        }
        Integer lowStock = product.getLowStock();
        if (lowStock != null && lowStock.compareTo(0) < 0) {
            throw new IllegalProductException("库存预警值小于0: " + lowStock);
        }
        String unit = product.getUnit();
        if (unit != null && unit.length() > Product.UNIT_MAX_LENGTH) {
            throw new IllegalProductException("商品计量单位长度大于" + Product.UNIT_MAX_LENGTH + ": " + unit);
        }
        Integer giftPoint = product.getGiftPoint();
        if (giftPoint != null && giftPoint.compareTo(0) < 0) {
            throw new IllegalProductException("赠送的积分小于0: " + giftPoint);
        }
        Integer usePointLimit = product.getUsePointLimit();
        if (usePointLimit != null && usePointLimit.compareTo(0) < 0) {
            throw new IllegalProductException("限制使用的积分数小于0: " + usePointLimit);
        }
        Integer promotionType = product.getPromotionType();
        if (promotionType != null && !Arrays.stream(Product.PromotionType.values())
            .map(Product.PromotionType::getValue).collect(Collectors.toList())
            .contains(promotionType)) {
            throw new IllegalProductException("促销类型不合法: " + promotionType);
        }
        Integer isPublish = product.getIsPublish();
        if (isPublish != null && !Arrays.stream(Product.PublishStatus.values())
                .map(Product.PublishStatus::getValue).collect(Collectors.toList())
                .contains(isPublish)) {
            throw new IllegalProductException("上架状态不合法: " + isPublish);
        }
        Integer isNew = product.getIsNew();
        if (isNew != null && !Arrays.stream(Product.NewStatus.values())
                .map(Product.NewStatus::getValue).collect(Collectors.toList())
                .contains(isNew)) {
            throw new IllegalProductException("新品状态不合法: " + isNew);
        }
        Integer isRecommend = product.getIsRecommend();
        if (isRecommend != null && !Arrays.stream(Product.RecommendStatus.values())
                .map(Product.RecommendStatus::getValue).collect(Collectors.toList())
                .contains(isRecommend)) {
            throw new IllegalProductException("推荐状态不合法: " + isRecommend);
        }
        Integer isVerify = product.getIsVerify();
        if (isVerify != null && !Arrays.stream(Product.VerifyStatus.values())
                .map(Product.VerifyStatus::getValue).collect(Collectors.toList())
                .contains(isVerify)) {
            throw new IllegalProductException("审核状态不合法: " + isVerify);
        }
        Integer isPreview = product.getIsPreview();
        if (isPreview != null && !Arrays.stream(Product.PreviewStatus.values())
                .map(Product.PreviewStatus::getValue).collect(Collectors.toList())
                .contains(isPreview)) {
            throw new IllegalProductException("预告状态不合法: " + isPreview);
        }
    }

    @Override
    public void validateProduct(Long id) throws IllegalProductException {
        if (productMapper.selectById(id) == null) {
            throw new IllegalProductException("商品不存在：" + id);
        }
    }

    @Override
    public ProductDTO getProductById(Long id) {
        // 商品信息
        Product product = productMapper.selectById(id);
        if (product == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProduct(product);
        List<ProductImage> productImages = productImageMapper.selectByProductId(id);
        productDTO.setProductImages(productImages);
        // 商品参数
        List<ProductParam> productParams = productParamService.getByProductId(id);
        productDTO.setProductParams(productParams);
        // 商品规格
        List<ProductDTO.SpecificationDTO> specificationDTOS = productSpecificationService.getByProductId(id);
        productDTO.setSpecifications(specificationDTOS);
        // sku
        List<ProductDTO.SkuDTO> skuDTOList = skuService.getByProductId(id);
        productDTO.setSkus(skuDTOList);
        return productDTO;
    }

    @Override
    public void updateProduct(Long id, UpdateProductParam param) throws IllegalProductException,
            IllegalProductCategoryException, IllegalBrandException {
        validateProduct(id);
        Product product = new Product();
        product.setId(id);
        product.setUpdateTime(new Date());
        BeanUtils.copyProperties(param, product);
        validateProduct(product);
        completeProduct(product);
        productMapper.updateById(product);
        // 更新商品图片
        productImageMapper.deleteByProductId(id);
        if (!CollectionUtils.isEmpty(param.getImages())) {
            List<ProductImage> productImages = param.getImages().stream()
                    .map(source -> {
                        ProductImage target = new ProductImage();
                        BeanUtils.copyProperties(source, target);
                        target.setProductId(id);
                        target.setCreateTime(new Date());
                        return target;
                    }).collect(Collectors.toList());
            productImageMapper.insertList(productImages);
        }
        productPublisher.publishUpdateMsg(id);
    }

    @Override
    public void updateParam(Long id, UpdateProductParamParam param) throws IllegalProductException, IllegalProductParamException {
        validateProduct(id);
        // 检查商品参数合法性
        List<ProductParam> productParams = param.getProductParams().stream()
                .map(source -> {
                    ProductParam target = new ProductParam();
                    BeanUtils.copyProperties(source, target);
                    return target;
                }).collect(Collectors.toList());
        productParams.forEach(productParamService::validate);

        List<ProductParam> oldProductParams = productParamService.getByProductId(id);
        if (oldProductParams == null) oldProductParams = new ArrayList<>();
        List<Long> oldParamIds = oldProductParams.stream().map(Base::getId).collect(Collectors.toList());
        List<Long> paramIds = productParams.stream().map(Base::getId).collect(Collectors.toList());
        // 删除商品参数
        List<Long> deleteParamIds = oldParamIds.stream()
                .filter(paramId -> !paramIds.contains(paramId))
                .collect(Collectors.toList());
        productParamService.delete(deleteParamIds);
        // 更新商品参数
        List<ProductParam> updateProductParams = productParams.stream()
                .filter(productParam -> productParam.getId() != null)
                .filter(productParam -> oldParamIds.contains(productParam.getId()))
                .collect(Collectors.toList());
        updateProductParams.forEach(productParam -> productParam.setUpdateTime(new Date()));
        productParamService.update(updateProductParams);
        // 新增商品参数
        List<ProductParam> addProductParams = productParams.stream()
                .filter(productParam -> productParam.getId() == null)
                .collect(Collectors.toList());
        addProductParams.forEach(productParam -> {
            productParam.setProductId(id);
            productParam.setCreateTime(new Date());
        });
        productParamService.save(addProductParams);
        productPublisher.publishUpdateMsg(id);
    }

    @Override
    public void updateSpecification(Long id, UpdateProductSpecificationParam param) throws IllegalProductException,
            IllegalProductSpecificationException {
        validateProduct(id);
        List<ProductSpecificationValue> allSpecificationValues = new ArrayList<>();
        // 检查商品规格合法性
        for (UpdateProductSpecificationParam.UpdateSpecification updateSpecification : param.getSpecifications()) {
            Long specificationId = updateSpecification.getId();
            ProductSpecification specification = productSpecificationService.getSpecificationById(specificationId);
            if (specification == null) {
                throw new IllegalProductSpecificationException("商品规格不存在：" + specificationId);
            }
            if (!Objects.equals(id, specification.getProductId())) {
                throw new IllegalProductSpecificationException("规格不属于该商品");
            }
            if (!CollectionUtils.isEmpty(updateSpecification.getValues())) {
                List<ProductSpecificationValue> specificationValues = updateSpecification.getValues().stream().map(source -> {
                    ProductSpecificationValue target = new ProductSpecificationValue();
                    target.setProductSpecificationId(specificationId);
                    target.setCreateTime(new Date());
                    BeanUtils.copyProperties(source, target);
                    return target;
                }).collect(Collectors.toList());
                specificationValues.forEach(productSpecificationService::validate);
                allSpecificationValues.addAll(specificationValues);
            }
        }
        productSpecificationService.saveSpecificationValues(allSpecificationValues);
        productPublisher.publishUpdateMsg(id);
    }

    @Override
    public void updateSku(Long id, UpdateSkuParam param) throws IllegalProductException, IllegalSkuException, IllegalProductSpecificationException {
        validateProduct(id);
        // 新增的sku
        List<UpdateSkuParam.UpdateSku> addSkuParams = param.getSkus().stream()
                .filter(sku -> sku.getId() == null).collect(Collectors.toList());
        List<Sku> addSkus = new ArrayList<>();
        for (int index = 0; index < addSkuParams.size(); index++) {
            UpdateSkuParam.UpdateSku source = addSkuParams.get(index);
            Sku target = new Sku();
            target.setProductId(id);
            target.setCreateTime(new Date());
            BeanUtils.copyProperties(source, target);
            if (target.getSkuCode() == null) {
                target.setSkuCode(skuService.generateSkuCode(target, index));
            }
            addSkus.add(target);
        }
        // 更新的sku
        List<UpdateSkuParam.UpdateSku> updateSkuParams = param.getSkus().stream()
                .filter(sku -> sku.getId() != null).collect(Collectors.toList());
        List<Sku> updateSkus = new ArrayList<>();
        for (UpdateSkuParam.UpdateSku source : updateSkuParams) {
            Sku target = new Sku();
            target.setUpdateTime(new Date());
            BeanUtils.copyProperties(source, target);
            updateSkus.add(target);
        }
        // 检查sku合法性
        addSkus.forEach(skuService::validate);
        updateSkus.forEach(skuService::validate);
        // 检查新增的sku的商品规格合法性
        if (!CollectionUtils.isEmpty(addSkus)) {
            List<ProductDTO.SpecificationDTO> specificationDTOS = productSpecificationService.getByProductId(id);
            for (UpdateSkuParam.UpdateSku updateSku : addSkuParams) {
                if (CollectionUtils.isEmpty(updateSku.getSpecifications())) {
                    throw new IllegalProductSpecificationException("sku的商品规格为空");
                }
                if (updateSku.getSpecifications().size() != specificationDTOS.size()) {
                    throw new IllegalProductSpecificationException("sku的商品规格数量不合法");
                }
                for (ProductDTO.SpecificationDTO specificationDTO : specificationDTOS) {
                    boolean isSpecificationExist = false;
                    for (UpdateSkuParam.UpdateSkuSpecificationRelationship relationship : updateSku.getSpecifications()) {
                        Long specificationId = relationship.getSpecificationId();
                        Long specificationValueId = relationship.getSpecificationValueId();
                        if (specificationDTO.getId().equals(specificationId)) {
                            isSpecificationExist = true;
                            boolean isSpecificationValueExist = false;
                            for (ProductSpecificationValue specificationValue : specificationDTO.getSpecificationValues()) {
                                if (specificationValue.getId().equals(specificationValueId)) {
                                    isSpecificationValueExist = true;
                                    break;
                                }
                            }
                            if (!isSpecificationValueExist) {
                                throw new IllegalProductSpecificationException("商品规格" + specificationId + "选项不存在：" + specificationValueId);
                            }
                            break;
                        }
                    }
                    if (!isSpecificationExist) {
                        throw new IllegalProductSpecificationException("缺少商品规格");
                    }
                }
            }
        }

        // 保存新增的sku
        List<SkuImage> allAddSkuImages = new ArrayList<>();
        List<SkuSpecificationRelationship> allAddSkuSpecificationRelationships = new ArrayList<>();
        for (int index = 0; index < addSkus.size(); index++) {
            UpdateSkuParam.UpdateSku updateSku = addSkuParams.get(index);
            List<ProductSpecificationDTO> specificationDTOS = updateSku.getSpecifications().stream().map(source -> {
                ProductSpecificationDTO target = new ProductSpecificationDTO();
                target.setSpecification(productSpecificationService.getSpecificationById(source.getSpecificationId()));
                target.setSpecificationValue(productSpecificationService.getSpecificationValueById(source.getSpecificationValueId()));
                return target;
            }).collect(Collectors.toList());
            Sku sku = addSkus.get(index);
            sku.setSpecification(skuService.generateSpecification(specificationDTOS));
            skuMapper.insert(sku);
            if (!CollectionUtils.isEmpty(updateSku.getImages())) {
                List<SkuImage> skuImages = updateSku.getImages().stream().map(source -> {
                    SkuImage target = new SkuImage();
                    target.setSkuId(sku.getId());
                    target.setCreateTime(new Date());
                    BeanUtils.copyProperties(source, target);
                    return target;
                }).collect(Collectors.toList());
                allAddSkuImages.addAll(skuImages);
            }
            if (!CollectionUtils.isEmpty(updateSku.getSpecifications())) {
                List<SkuSpecificationRelationship> relationships = updateSku.getSpecifications().stream().map(source -> {
                    SkuSpecificationRelationship target = new SkuSpecificationRelationship();
                    target.setSkuId(sku.getId());
                    target.setCreateTime(new Date());
                    BeanUtils.copyProperties(source, target);
                    return target;
                }).collect(Collectors.toList());
                allAddSkuSpecificationRelationships.addAll(relationships);
            }
        }
        skuService.saveSkuImages(allAddSkuImages);
        skuService.saveSkuSpecificationRelationships(allAddSkuSpecificationRelationships);
        // 保存更新的sku
        List<SkuImage> allUpdateSkuImages = new ArrayList<>();
        for (int index = 0; index < updateSkus.size(); index++) {
            UpdateSkuParam.UpdateSku updateSku = updateSkuParams.get(index);
            Sku sku = updateSkus.get(index);
            skuMapper.updateById(sku);
            // 图片列表不为null时用新图片替换旧图片
            List<UpdateSkuParam.UpdateSkuImage> updateSkuImages = updateSku.getImages();
            if (updateSkuImages != null) {
                // 删除旧图片
                skuService.deleteSkuImage(sku.getId());
                // 获取新图片
                List<SkuImage> skuImages = updateSkuImages.stream().map(source -> {
                    SkuImage target = new SkuImage();
                    target.setSkuId(sku.getId());
                    target.setCreateTime(new Date());
                    BeanUtils.copyProperties(source, target);
                    return target;
                }).collect(Collectors.toList());
                allUpdateSkuImages.addAll(skuImages);
            }
        }
        skuService.saveSkuImages(allUpdateSkuImages);
        productPublisher.publishUpdateMsg(id);
    }

    @Override
    public CommonPage<Product> list(Long pageNum, Long pageSize) {
        Long count = getProductExplainCount();
        List<Product> products = productMapper.selectByPage((pageNum - 1) * pageSize, pageSize);
        return new CommonPage<>(pageNum, pageSize, count / pageSize, count, products);
    }

    @Override
    public void updateProductBatch(List<Long> ids, UpdateProductBatchParam param) throws IllegalProductException,
            IllegalProductCategoryException, IllegalBrandException {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        ids = ids.stream().filter(id -> productMapper.selectById(id) != null).collect(Collectors.toList());
        Product product = new Product();
        BeanUtils.copyProperties(param, product);
        validateProduct(product);
        completeProduct(product);
        productMapper.updateByIds(ids, product);
        ids.forEach(productPublisher::publishUpdateMsg);
    }

    @Override
    public void updatePublishStatus(List<Long> ids, Integer publishStatus) throws IllegalProductException {
        UpdateProductBatchParam param = new UpdateProductBatchParam();
        param.setIsPublish(publishStatus);
        updateProductBatch(ids, param);
    }

    @Override
    public void updateRecommendStatus(List<Long> ids, Integer recommendStatus) throws IllegalProductException {
        UpdateProductBatchParam param = new UpdateProductBatchParam();
        param.setIsRecommend(recommendStatus);
        updateProductBatch(ids, param);
    }

    @Override
    public void updateNewStatus(List<Long> ids, Integer newStatus) throws IllegalProductException {
        UpdateProductBatchParam param = new UpdateProductBatchParam();
        param.setIsNew(newStatus);
        updateProductBatch(ids, param);
    }

    @Override
    public List<Sku> querySku(Long id, String keyword) {
        keyword = (keyword == null) ? "" : keyword;
        return skuMapper.queryByProductIdAndSkuCode(id, keyword);
    }

    @Override
    public void verifyProduct(Long id, Integer verifyStatus, String note) throws IllegalProductException {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new IllegalProductException("商品不存在");
        }
        if (!Arrays.stream(Product.VerifyStatus.values())
                .map(Product.VerifyStatus::getValue)
                .collect(Collectors.toList()).contains(verifyStatus)) {
            throw new IllegalProductException("审核状态不合法");
        }
        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setUpdateTime(new Date());
        updateProduct.setIsVerify(verifyStatus);
        productMapper.updateById(updateProduct);
        productVerifyLogService.saveLog(id, note, verifyStatus);
        productPublisher.publishUpdateMsg(id);
    }

    @Override
    public CommonPage<Product> queryProduct(QueryProductParam param, Long pageNum, Long pageSize) {
        List<Product> products = productMapper.query((pageNum - 1) * pageSize, pageSize, param);
        Long count = productMapper.explainQuery(param).getRows();
        count = (count == null ? 0 : count);
        return new CommonPage<>(pageNum, pageSize, count / pageSize, count, products);
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.PRODUCT_CATEGORY_UPDATE_QUEUE_PRODUCT)
    public void listenProductCategoryUpdate(Channel channel, Message message) throws IOException {
        Long productCategoryId;
        ProductCategory productCategory;
        try {
            UpdateProductCategoryMessageDTO messageDTO = objectMapper.readValue(message.getBody(),
                    UpdateProductCategoryMessageDTO.class);
            productCategoryId = messageDTO.getId();
            productCategory = productCategoryService.getById(productCategoryId);
            if (productCategory == null) {
                throw new NullPointerException();
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        Product product = new Product();
        product.setProductCategoryId(productCategoryId);
        product.setProductCategoryName(productCategory.getName());
        product.setUpdateTime(new Date());
        productMapper.updateByProductCategoryId(productCategoryId, product);
        List<Long> productIds = productMapper.selectIdsByProductCategoryId(productCategoryId);
        productIds.forEach(productPublisher::publishUpdateMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.PRODUCT_CATEGORY_DELETE_QUEUE_PRODUCT)
    public void listenProductCategoryDelete(Channel channel, Message message) throws IOException {
        Long productCategoryId = null;
        try {
            DeleteProductCategoryMessageDTO messageDTO = objectMapper.readValue(message.getBody(),
                    DeleteProductCategoryMessageDTO.class);
            productCategoryId = messageDTO.getId();
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
        List<Long> productIds = productMapper.selectIdsByProductCategoryId(productCategoryId);
        Product product = new Product();
        product.setProductCategoryId(Product.NON_PRODUCT_CATEGORY_ID);
        product.setProductCategoryName("");
        product.setUpdateTime(new Date());
        productMapper.updateByProductCategoryId(productCategoryId, product);
        productIds.forEach(productPublisher::publishUpdateMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.BRAND_UPDATE_QUEUE_PRODUCT)
    public void listenBrandUpdate(Channel channel, Message message) throws IOException {
        Long brandId = null;
        String brandName = null;
        try {
            UpdateBrandMessageDTO messageDTO = objectMapper.readValue(message.getBody(), UpdateBrandMessageDTO.class);
            brandId = messageDTO.getId();
            brandName = brandService.getById(brandId).getName();
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
        Product product = new Product();
        product.setBrandName(brandName);
        product.setUpdateTime(new Date());
        productMapper.updateByBrandId(brandId, product);
        List<Long> productIds = productMapper.selectIdsByBrandId(brandId);
        productIds.forEach(productPublisher::publishUpdateMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    @RabbitListener(queues = RabbitmqConfig.BRAND_DELETE_QUEUE_PRODUCT)
    public void listenBrandDelete(Channel channel, Message message) throws IOException {
        Long brandId = null;
        try {
            DeleteBrandMessageDTO messageDTO = objectMapper.readValue(message.getBody(), DeleteBrandMessageDTO.class);
            brandId = messageDTO.getId();
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
        List<Long> productIds = productMapper.selectIdsByBrandId(brandId);
        Product product = new Product();
        product.setBrandId(Product.NON_BRAND_ID);
        product.setBrandName("");
        product.setUpdateTime(new Date());
        productMapper.updateByBrandId(brandId, product);
        productIds.forEach(productPublisher::publishUpdateMsg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 保存新增商品的信息
     * @param createProduct 新增商品的信息
     * @return 保存成功后的商品信息
     */
    private Product saveCreateProduct(CreateProductParam.CreateProduct createProduct) {
        Product product = new Product();
        BeanUtils.copyProperties(createProduct, product);
        completeProduct(product);
        product.setCreateTime(new Date());
        productMapper.insert(product);
        // 保存商品图片
        if (!CollectionUtils.isEmpty(createProduct.getImages())) {
            List<ProductImage> images = createProduct.getImages().stream()
                    .map(source -> {
                        ProductImage target = new ProductImage();
                        target.setProductId(product.getId());
                        target.setCreateTime(new Date());
                        BeanUtils.copyProperties(source, target);
                        return target;
                    }).collect(Collectors.toList());
            productImageMapper.insertList(images);
        }
        return product;
    }

    /**
     * 保存新增商品的商品参数信息
     * @param createParams 新增商品的商品参数信息
     */
    private void saveCreateProductParam(List<CreateProductParam.CreateParam> createParams, Long productId) {
        if (CollectionUtils.isEmpty(createParams)) {
            return;
        }
        List<ProductParam> productParams = createParams.stream()
                .map(source -> {
                    ProductParam target = new ProductParam();
                    BeanUtils.copyProperties(source, target);
                    target.setProductId(productId);
                    target.setCreateTime(new Date());
                    return target;
                }).collect(Collectors.toList());
        productParamService.save(productParams);
    }

    /**
     * 保存新增商品的商品规格信息，并返回保存成功后
     * 的商品规格信息，并且商品规格列表索引顺序不变。
     * 如果 {@code createSpecifications} 是空或null，则返回null
     * @param productId 商品id
     * @param createSpecifications 商品规格信息
     * @return 保存成功的商品规格信息
     */
    private List<ProductSpecification> saveCreateSpecifications(Long productId, List<CreateProductParam.CreateSpecification> createSpecifications) {
        if (CollectionUtils.isEmpty(createSpecifications)) {
            return null;
        }
        List<ProductSpecification> specifications = createSpecifications.stream()
                .map(source -> {
                    ProductSpecification target = new ProductSpecification();
                    BeanUtils.copyProperties(source, target);
                    target.setProductId(productId);
                    target.setCreateTime(new Date());
                    return target;
                }).collect(Collectors.toList());
        return productSpecificationService.saveSpecifications(specifications);
    }

    /**
     * 保存新增商品的商品规格选项，并返回保存成功后的
     * 商品规格选项信息，并且商品规格选项列表索引顺序不变。
     * 如果 {@code specifications} 为空或null，则返回null。
     * @param specifications 已经保存成功的商品规格
     * @param createSpecifications 商品规格信息
     * @return 保存成功的商品规格选项信息
     */
    private List<List<ProductSpecificationValue>> saveCreateSpecificationValues(List<ProductSpecification> specifications,
                                                                                List<CreateProductParam.CreateSpecification> createSpecifications) {
        if (CollectionUtils.isEmpty(specifications)) {
            return null;
        }
        List<List<ProductSpecificationValue>> result = new ArrayList<>();
        for (int index = 0; index < createSpecifications.size(); index++) {
            ProductSpecification specification = specifications.get(index);
            CreateProductParam.CreateSpecification createSpecification = createSpecifications.get(index);
            List<ProductSpecificationValue> specificationValues = createSpecification.getValues().stream()
                    .map(source -> {
                        ProductSpecificationValue target = new ProductSpecificationValue();
                        BeanUtils.copyProperties(source, target);
                        target.setProductSpecificationId(specification.getId());
                        target.setCreateTime(new Date());
                        return target;
                    }).collect(Collectors.toList());
            List<ProductSpecificationValue> save = productSpecificationService.saveSpecificationValues(specificationValues);
            result.add(save);
        }
        return result;
    }

    /**
     * 保存新增商品的sku信息
     * @param createSkus sku信息
     * @param specifications 新增商品的规格信息
     * @param specificationValueLists 新增商品的规格选项信息，索引为0的规格选项列表对应 {@code specifications}
     *                                索引为0的规格，以此类推。
     * @param productId 商品id
     */
    private void saveCreateSkus(List<CreateProductParam.CreateSku> createSkus, List<ProductSpecification> specifications,
                                List<List<ProductSpecificationValue>> specificationValueLists, Long productId) {
        if (CollectionUtils.isEmpty(createSkus)) {
            return;
        }
        List<SkuImage> images = new ArrayList<>();
        List<SkuSpecificationRelationship> skuSpecificationRelationships = new ArrayList<>();
        for (int skuIndex = 0; skuIndex < createSkus.size(); skuIndex++) {
            CreateProductParam.CreateSku createSku = createSkus.get(skuIndex);
            Sku sku = new Sku();
            BeanUtils.copyProperties(createSku, sku);
            sku.setProductId(productId);
            sku.setCreateTime(new Date());
            if (sku.getSkuCode() == null) {
                sku.setSkuCode(skuService.generateSkuCode(sku, skuIndex));
            }
            List<SkuSpecificationRelationship> thisSkuSpecificationRelationships = new ArrayList<>();
            List<ProductSpecificationDTO> specificationDTOS = new ArrayList<>();
            List<Integer> specificationIndex = createSku.getSpecificationIndex();
            for (int index = 0; index < specificationIndex.size(); index++) {
                Integer valueIndex = specificationIndex.get(index);
                ProductSpecification specification = specifications.get(index);
                ProductSpecificationValue specificationValue = specificationValueLists.get(index).get(valueIndex);
                ProductSpecificationDTO specificationDTO = new ProductSpecificationDTO(specification, specificationValue);
                specificationDTOS.add(specificationDTO);
                SkuSpecificationRelationship skuSpecificationRelationship = new SkuSpecificationRelationship();
                skuSpecificationRelationship.setSpecificationId(specification.getId());
                skuSpecificationRelationship.setSpecificationValueId(specificationValue.getId());
                thisSkuSpecificationRelationships.add(skuSpecificationRelationship);
            }
            sku.setSpecification(skuService.generateSpecification(specificationDTOS));
            skuMapper.insert(sku);
            thisSkuSpecificationRelationships.forEach(relationship -> relationship.setSkuId(sku.getId()));
            skuSpecificationRelationships.addAll(thisSkuSpecificationRelationships);
            if (!CollectionUtils.isEmpty(createSku.getImages())) {
                List<SkuImage> thisSkuImages = createSku.getImages().stream()
                        .map(source -> {
                            SkuImage target = new SkuImage();
                            BeanUtils.copyProperties(source, target);
                            target.setSkuId(sku.getId());
                            target.setCreateTime(new Date());
                            return target;
                        }).collect(Collectors.toList());
                images.addAll(thisSkuImages);
            }
        }
        skuService.saveSkuImages(images);
        skuService.saveSkuSpecificationRelationships(skuSpecificationRelationships);
    }

    /**
     * 完善商品的信息。
     * 1. 根据brandId完善brandName，若商品品牌不存在则设置为null，若brandName已经存在则不做操作。
     * 2. 根据productCategoryId完善productCategoryName，若商品分类不存在则设置为null，若productCategoryName已经存在则不做操作。
     * @param product 商品信息
     */
    private void completeProduct(Product product) {
        if (product.getBrandName() == null) {
            Brand brand = brandService.getById(product.getBrandId());
            product.setBrandName(brand == null ? null : brand.getName());
        }
        if (product.getProductCategoryName() == null) {
            ProductCategory productCategory = productCategoryService.getById(product.getProductCategoryId());
            product.setProductCategoryName(productCategory == null ? null : productCategory.getName());
        }
    }

    /**
     * 检查商品规格合法性
     * @param specifications 商品规格
     * @throws IllegalProductSpecificationException 商品规格不合法
     */
    private void validateSpecifications(List<CreateProductParam.CreateSpecification> specifications)
            throws IllegalProductSpecificationException {
        if (!CollectionUtils.isEmpty(specifications)) {
            for (CreateProductParam.CreateSpecification specification : specifications) {
                if (!CollectionUtils.isEmpty(specification.getValues())) {
                    List<ProductSpecificationValue> specificationValues = specification.getValues().stream()
                            .map(source -> {
                                ProductSpecificationValue target = new ProductSpecificationValue();
                                BeanUtils.copyProperties(source, target);
                                return target;
                            }).collect(Collectors.toList());
                    specificationValues.forEach(productSpecificationService::validate);
                }
                else {
                    throw new IllegalProductSpecificationException(specification.getName() + "商品规格的选项为空");
                }
            }
        }
    }

    /**
     * 检查商品参数合法性
     * @param createParams 商品参数
     * @throws IllegalProductParamException 商品参数不合法
     */
    private void validateProductParams(List<CreateProductParam.CreateParam> createParams)
            throws IllegalProductParamException {
        if (!CollectionUtils.isEmpty(createParams)) {
            List<ProductParam> params = createParams.stream()
                    .map(source -> {
                        ProductParam target = new ProductParam();
                        BeanUtils.copyProperties(source, target);
                        return target;
                    }).collect(Collectors.toList());
            params.forEach(productParamService::validate);
        }
    }

    /**
     * 检查sku合法性
     * @param skus sku信息
     * @param specifications 商品规格信息
     * @throws IllegalSkuException sku不合法
     */
    private void validateSkus(List<CreateProductParam.CreateSku> skus,
                              List<CreateProductParam.CreateSpecification> specifications)
        throws IllegalSkuException {
        if (!CollectionUtils.isEmpty(skus)) {
            for (CreateProductParam.CreateSku createSku : skus) {
                Sku sku = new Sku();
                BeanUtils.copyProperties(createSku, sku);
                skuService.validate(sku);
                List<Integer> specificationIndex = createSku.getSpecificationIndex();
                if (CollectionUtils.isEmpty(specificationIndex)) {
                    throw new IllegalSkuException(createSku + " 该sku的商品规格为空");
                }
                if (specificationIndex.size() != specifications.size()) {
                    throw new IllegalSkuException(createSku + " 该sku与商品规格数量不匹配");
                }
                for (int index = 0; index < specificationIndex.size(); index++) {
                    Integer valueIndex = specificationIndex.get(index);
                    try {
                        if (specifications.get(index).getValues().get(valueIndex) == null) {
                            throw new IllegalSkuException(createSku + " 该sku的商品规格不存在");
                        }
                    } catch (NullPointerException | IndexOutOfBoundsException e) {
                        throw new IllegalSkuException(createSku + " 该sku的商品规格不存在");
                    }
                }
            }
        }
    }

    /**
     * 根据mysql explain获取商品的数量
     * @return 商品的大致数量
     */
    private long getProductExplainCount() {
        MysqlExplain mysqlExplain = productMapper.explain();
        return mysqlExplain.getRows() == null ? 0 : mysqlExplain.getRows();
    }

    @Autowired
    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    @Autowired
    public void setBrandService(BrandService brandService) {
        this.brandService = brandService;
    }

    @Autowired
    public void setProductPublisher(ProductPublisher productPublisher) {
        this.productPublisher = productPublisher;
    }

    @Autowired
    public void setProductParamService(ProductParamService productParamService) {
        this.productParamService = productParamService;
    }

    @Autowired
    public void setProductCategoryService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Autowired
    public void setProductVerifyLogService(ProductVerifyLogService productVerifyLogService) {
        this.productVerifyLogService = productVerifyLogService;
    }

    @Autowired
    public void setProductSpecificationService(ProductSpecificationService productSpecificationService) {
        this.productSpecificationService = productSpecificationService;
    }
}
