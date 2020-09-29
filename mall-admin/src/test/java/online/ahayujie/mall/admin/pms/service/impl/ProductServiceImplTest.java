package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.bean.model.*;
import online.ahayujie.mall.admin.pms.exception.*;
import online.ahayujie.mall.admin.pms.mapper.*;
import online.ahayujie.mall.admin.pms.service.ProductService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductServiceImplTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductImageMapper productImageMapper;
    @Autowired
    private ProductParamMapper productParamMapper;
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationValueMapper productSpecificationValueMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuSpecificationRelationshipMapper skuSpecificationRelationshipMapper;

    @Test
    void create() {
        // 测试商品规格合法性检查
        testProductSpecificationValidate();
        // 测试商品参数合法性检查
        testProductParamsValidate();
        // 测试sku合法性检查
        testSkusValidate();
        // 测试插入商品信息
        testSaveCreateProduct();
        // 测试插入商品参数信息
        testSaveCreateParams();
        // 测试插入商品规格信息
        testSaveCreateSpecifications();
        // 测试插入商品sku信息
        testSaveCreateSkus();
    }

    private void testProductSpecificationValidate() {
        // illegal
        // 商品规格选项值为空
        CreateProductParam param = new CreateProductParam();
        param.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications = new ArrayList<>();
        CreateProductParam.CreateSpecification specification = new CreateProductParam.CreateSpecification();
        specification.setName("for test");
        specification.setValues(new ArrayList<>());
        specifications.add(specification);
        param.setSpecifications(specifications);
        Throwable throwable = assertThrows(IllegalProductSpecificationException.class, () -> productService.create(param));
        log.debug(throwable.getMessage());
        // 商品规格选项值类型不合法
        CreateProductParam param1 = new CreateProductParam();
        param1.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications1 = new ArrayList<>();
        CreateProductParam.CreateSpecification specification1 = new CreateProductParam.CreateSpecification();
        specification1.setName("for test");
        CreateProductParam.CreateSpecificationValue specificationValue = new CreateProductParam.CreateSpecificationValue();
        specificationValue.setType(-1);
        specification1.setValues(Collections.singletonList(specificationValue));
        specifications1.add(specification1);
        param1.setSpecifications(specifications1);
        Throwable throwable1 = assertThrows(IllegalProductSpecificationException.class, () -> productService.create(param1));
        log.debug(throwable1.getMessage());
        // 纯文本类型的商品规格选项值内容不合法
        CreateProductParam param2 = new CreateProductParam();
        param2.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications2 = new ArrayList<>();
        CreateProductParam.CreateSpecification specification2 = new CreateProductParam.CreateSpecification();
        specification2.setName("for test");
        CreateProductParam.CreateSpecificationValue specificationValue1 = new CreateProductParam.CreateSpecificationValue();
        specificationValue1.setType(ProductSpecificationValue.Type.TEXT.getValue());
        specificationValue1.setValue("{\"test\" : \"test\"}");
        specification2.setValues(Collections.singletonList(specificationValue1));
        specifications2.add(specification2);
        param2.setSpecifications(specifications2);
        Throwable throwable2 = assertThrows(IllegalProductSpecificationException.class, () -> productService.create(param2));
        log.debug(throwable2.getMessage());
        // 图文类型的商品规格选项值内容不合法
        CreateProductParam param3 = new CreateProductParam();
        param3.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications3 = new ArrayList<>();
        CreateProductParam.CreateSpecification specification3 = new CreateProductParam.CreateSpecification();
        specification3.setName("for test");
        CreateProductParam.CreateSpecificationValue specificationValue2 = new CreateProductParam.CreateSpecificationValue();
        specificationValue2.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        specificationValue2.setValue("{\"test\" : \"test\"}");
        specification3.setValues(Collections.singletonList(specificationValue2));
        specifications3.add(specification3);
        param3.setSpecifications(specifications3);
        Throwable throwable3 = assertThrows(IllegalProductSpecificationException.class, () -> productService.create(param3));
        log.debug(throwable3.getMessage());
    }

    private void testProductParamsValidate() {
        // illegal
        // 商品参数类型不合法
        CreateProductParam param = new CreateProductParam();
        param.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateParam> params = new ArrayList<>();
        CreateProductParam.CreateParam createParam = new CreateProductParam.CreateParam();
        createParam.setName("for test");
        createParam.setType(-1);
        createParam.setValue("for test");
        params.add(createParam);
        param.setParams(params);
        Throwable throwable = assertThrows(IllegalProductParamException.class, () -> productService.create(param));
        log.debug(throwable.getMessage());
        // 纯文本类型的商品参数内容不合法
        CreateProductParam param1 = new CreateProductParam();
        param1.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateParam> params1 = new ArrayList<>();
        CreateProductParam.CreateParam createParam1 = new CreateProductParam.CreateParam();
        createParam1.setName("for test");
        createParam1.setType(ProductParam.Type.TEXT.getValue());
        createParam1.setValue("{\"test\" : \"test\"}");
        params1.add(createParam1);
        param1.setParams(params1);
        Throwable throwable1 = assertThrows(IllegalProductParamException.class, () -> productService.create(param1));
        log.debug(throwable1.getMessage());
        // 图文类型的商品参数内容不合法
        CreateProductParam param2 = new CreateProductParam();
        param2.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateParam> params2 = new ArrayList<>();
        CreateProductParam.CreateParam createParam2 = new CreateProductParam.CreateParam();
        createParam2.setName("for test");
        createParam2.setType(ProductParam.Type.IMAGE_TEXT.getValue());
        createParam2.setValue("{\"test\" : \"test\"}");
        params2.add(createParam2);
        param2.setParams(params2);
        Throwable throwable2 = assertThrows(IllegalProductParamException.class, () -> productService.create(param2));
        log.debug(throwable2.getMessage());

        // legal
        CreateProductParam param3 = new CreateProductParam();
        param3.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateParam> params3 = new ArrayList<>();
        CreateProductParam.CreateParam createParam3 = new CreateProductParam.CreateParam();
        createParam3.setName("for test");
        createParam3.setType(ProductParam.Type.TEXT.getValue());
        createParam3.setValue("{\"name\" : \"test\"}");
        params3.add(createParam3);
        param3.setParams(params3);
        productService.create(param3);
        CreateProductParam param4 = new CreateProductParam();
        param4.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateParam> params4 = new ArrayList<>();
        CreateProductParam.CreateParam createParam4 = new CreateProductParam.CreateParam();
        createParam4.setName("for test");
        createParam4.setType(ProductParam.Type.IMAGE_TEXT.getValue());
        createParam4.setValue("{\"name\" : \"test\", \"image\" : \"test\"}");
        params4.add(createParam4);
        param4.setParams(params4);
        productService.create(param4);
    }

    private void testSkusValidate() {
        // illegal
        // sku的商品规格为空
        CreateProductParam param = new CreateProductParam();
        param.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSku> skus = new ArrayList<>();
        CreateProductParam.CreateSku sku = new CreateProductParam.CreateSku();
        sku.setSpecificationIndex(null);
        skus.add(sku);
        param.setSkus(skus);
        Throwable throwable = assertThrows(IllegalSkuException.class, () -> productService.create(param));
        log.debug(throwable.getMessage());
        // sku与商品规格数量不匹配
        CreateProductParam param1 = new CreateProductParam();
        param1.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications = new ArrayList<>();
        int size = new Random().nextInt(5) + 1;
        for (int i = 0; i < size; i++) {
            CreateProductParam.CreateSpecification specification = new CreateProductParam.CreateSpecification();
            specification.setName("for test: " + i);
            CreateProductParam.CreateSpecificationValue value = new CreateProductParam.CreateSpecificationValue();
            value.setType(ProductSpecificationValue.Type.TEXT.getValue());
            value.setValue("{\"name\" : \"test\"}");
            specification.setValues(Collections.singletonList(value));
            specifications.add(specification);
        }
        param1.setSpecifications(specifications);
        List<CreateProductParam.CreateSku> skus1 = new ArrayList<>();
        CreateProductParam.CreateSku sku1 = new CreateProductParam.CreateSku();
        List<Integer> specificationIndex = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            specificationIndex.add(i);
        }
        sku1.setSpecificationIndex(specificationIndex);
        skus1.add(sku1);
        param1.setSkus(skus1);
        Throwable throwable1 = assertThrows(IllegalSkuException.class, () -> productService.create(param1));
        log.debug(throwable1.getMessage());
        // sku的商品规格不存在
        CreateProductParam param2 = new CreateProductParam();
        param2.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications1 = new ArrayList<>();
        CreateProductParam.CreateSpecification specification = new CreateProductParam.CreateSpecification();
        specification.setName("for test");
        CreateProductParam.CreateSpecificationValue value = new CreateProductParam.CreateSpecificationValue();
        value.setType(ProductSpecificationValue.Type.TEXT.getValue());
        value.setValue("{\"name\" : \"test\"}");
        specification.setValues(Collections.singletonList(value));
        specifications1.add(specification);
        param2.setSpecifications(specifications1);
        List<CreateProductParam.CreateSku> skus2 = new ArrayList<>();
        CreateProductParam.CreateSku sku2 = new CreateProductParam.CreateSku();
        List<Integer> specificationIndex1 = new ArrayList<>();
        specificationIndex1.add(specification.getValues().size());
        sku2.setSpecificationIndex(specificationIndex1);
        skus2.add(sku2);
        param2.setSkus(skus2);
        Throwable throwable2 = assertThrows(IllegalSkuException.class, () -> productService.create(param2));
        log.debug(throwable2.getMessage());

        // legal
        CreateProductParam param3 = new CreateProductParam();
        param3.setProduct(new CreateProductParam.CreateProduct());
        List<CreateProductParam.CreateSpecification> specifications2 = new ArrayList<>();
        CreateProductParam.CreateSpecification specification1 = new CreateProductParam.CreateSpecification();
        specification1.setName("for test");
        CreateProductParam.CreateSpecificationValue value1 = new CreateProductParam.CreateSpecificationValue();
        value1.setType(ProductSpecificationValue.Type.TEXT.getValue());
        value1.setValue("{\"name\" : \"test\"}");
        specification1.setValues(Collections.singletonList(value1));
        specifications2.add(specification1);
        CreateProductParam.CreateSpecification specification2 = new CreateProductParam.CreateSpecification();
        specification2.setName("for test2");
        CreateProductParam.CreateSpecificationValue value2 = new CreateProductParam.CreateSpecificationValue();
        value2.setType(ProductSpecificationValue.Type.TEXT.getValue());
        value2.setValue("{\"name\" : \"test2\"}");
        specification2.setValues(Collections.singletonList(value2));
        specifications2.add(specification2);
        param3.setSpecifications(specifications2);
        List<CreateProductParam.CreateSku> skus3 = new ArrayList<>();
        CreateProductParam.CreateSku sku3 = new CreateProductParam.CreateSku();
        List<Integer> specificationIndex2 = new ArrayList<>();
        specificationIndex2.add(specification1.getValues().size() - 1);
        specificationIndex2.add(specification2.getValues().size() - 1);
        sku3.setSpecificationIndex(specificationIndex2);
        skus3.add(sku3);
        param3.setSkus(skus3);
        productService.create(param3);
    }

    private void testSaveCreateProduct() {
        CreateProductParam param = new CreateProductParam();
        CreateProductParam.CreateProduct createProduct = getCreateProduct();
        param.setProduct(createProduct);
        List<Product> oldProducts = productMapper.selectAll();
        productService.create(param);
        List<Product> newProducts = productMapper.selectAll();
        assertEquals(oldProducts.size() + 1, newProducts.size());
        Product createdProduct = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                createdProduct = newProduct;
            }
        }
        assertNotNull(createdProduct);
        log.debug("createdProduct: " + createdProduct);
        Product cmpProduct = new Product();
        BeanUtils.copyProperties(createdProduct, cmpProduct);
        BeanUtils.copyProperties(createProduct, cmpProduct);
        assertEquals(cmpProduct, createdProduct);
        Brand brand = brandMapper.selectById(createdProduct.getBrandId());
        ProductCategory productCategory = productCategoryMapper.selectById(createdProduct.getProductCategoryId());
        assertEquals(brand.getName(), createdProduct.getBrandName());
        assertEquals(productCategory.getName(), createdProduct.getProductCategoryName());
        List<ProductImage> images = productImageMapper.selectByProductId(createdProduct.getId());
        assertEquals(createProduct.getImages().size(), images.size());
        log.debug("images : " + images);
    }

    private void testSaveCreateParams() {
        CreateProductParam createProductParam = new CreateProductParam();
        CreateProductParam.CreateProduct createProduct = getCreateProduct();
        createProductParam.setProduct(createProduct);
        List<CreateProductParam.CreateParam> createParams = new ArrayList<>();
        // 纯文本类型
        Random random = new Random();
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            CreateProductParam.CreateParam createParam = new CreateProductParam.CreateParam();
            createParam.setName("for test: " + i);
            createParam.setType(ProductParam.Type.TEXT.getValue());
            createParam.setValue("{\"name\" : \"test\"}");
            createParams.add(createParam);
        }
        // 图文类型
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            CreateProductParam.CreateParam createParam = new CreateProductParam.CreateParam();
            createParam.setName("for test: " + i);
            createParam.setType(ProductParam.Type.IMAGE_TEXT.getValue());
            createParam.setValue("{\"name\" : \"test\", \"image\" : \"test\"}");
            createParams.add(createParam);
        }
        createProductParam.setParams(createParams);
        List<Product> oldProducts = productMapper.selectAll();
        productService.create(createProductParam);
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
                break;
            }
        }
        assertNotNull(product);
        log.debug("product: " + product);
        List<ProductParam> productParams = productParamMapper.selectByProductId(product.getId());
        assertEquals(productParams.size(), createProductParam.getParams().size());
        log.debug("product params: " + productParams);
    }

    private void testSaveCreateSpecifications() {
        CreateProductParam createProductParam = new CreateProductParam();
        CreateProductParam.CreateProduct createProduct = getCreateProduct();
        createProductParam.setProduct(createProduct);
        List<CreateProductParam.CreateSpecification> createSpecifications = new ArrayList<>();
        // 纯文本类型商品规格
        CreateProductParam.CreateSpecification createSpecification = new CreateProductParam.CreateSpecification();
        createSpecification.setName("test1");
        List<CreateProductParam.CreateSpecificationValue> createSpecificationValues = new ArrayList<>();
        CreateProductParam.CreateSpecificationValue createSpecificationValue = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValue.setValue("{\"name\" : \"test\"}");
        createSpecificationValues.add(createSpecificationValue);
        CreateProductParam.CreateSpecificationValue createSpecificationValue1 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue1.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValue1.setValue("{\"name\" : \"test1\"}");
        createSpecificationValues.add(createSpecificationValue1);
        CreateProductParam.CreateSpecificationValue createSpecificationValue2 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue2.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValue2.setValue("{\"name\" : \"test2\"}");
        createSpecificationValues.add(createSpecificationValue2);
        createSpecification.setValues(createSpecificationValues);
        createSpecifications.add(createSpecification);
        // 图文类型商品规格
        CreateProductParam.CreateSpecification createSpecification1 = new CreateProductParam.CreateSpecification();
        createSpecification1.setName("test2");
        List<CreateProductParam.CreateSpecificationValue> createSpecificationValues1 = new ArrayList<>();
        CreateProductParam.CreateSpecificationValue createSpecificationValue3 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue3.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        createSpecificationValue3.setValue("{\"name\" : \"test\", \"image\" : \"test\"}");
        createSpecificationValues1.add(createSpecificationValue3);
        CreateProductParam.CreateSpecificationValue createSpecificationValue4 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue4.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        createSpecificationValue4.setValue("{\"name\" : \"test2\", \"image\" : \"test2\"}");
        createSpecificationValues1.add(createSpecificationValue4);
        createSpecification1.setValues(createSpecificationValues1);
        createSpecifications.add(createSpecification1);
        createProductParam.setSpecifications(createSpecifications);
        List<Product> oldProducts = productMapper.selectAll();
        productService.create(createProductParam);
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
                break;
            }
        }
        assertNotNull(product);
        log.debug("product: " + product);
        List<ProductSpecification> specifications = productSpecificationMapper.selectByProductId(product.getId());
        assertEquals(createProductParam.getSpecifications().size(), specifications.size());
        log.debug("specifications: " + specifications);
        int valueSize = 0;
        for (CreateProductParam.CreateSpecification specification : createProductParam.getSpecifications()) {
            valueSize += specification.getValues().size();
        }
        List<ProductSpecificationValue> specificationValues = new ArrayList<>();
        for (ProductSpecification specification : specifications) {
            specificationValues.addAll(productSpecificationValueMapper.selectBySpecificationId(specification.getId()));
        }
        assertEquals(valueSize, specificationValues.size());
        log.debug("specificationValues: " + specificationValues);
    }

    private void testSaveCreateSkus() {
        CreateProductParam createProductParam = new CreateProductParam();
        CreateProductParam.CreateProduct createProduct = getCreateProduct();
        createProductParam.setProduct(createProduct);
        List<CreateProductParam.CreateSpecification> createSpecifications = new ArrayList<>();
        // 纯文本类型商品规格
        CreateProductParam.CreateSpecification createSpecification = new CreateProductParam.CreateSpecification();
        createSpecification.setName("大小");
        List<CreateProductParam.CreateSpecificationValue> createSpecificationValues = new ArrayList<>();
        CreateProductParam.CreateSpecificationValue createSpecificationValue = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValue.setValue("{\"name\" : \"一般大\"}");
        createSpecificationValues.add(createSpecificationValue);
        CreateProductParam.CreateSpecificationValue createSpecificationValue1 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue1.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValue1.setValue("{\"name\" : \"还挺大\"}");
        createSpecificationValues.add(createSpecificationValue1);
        CreateProductParam.CreateSpecificationValue createSpecificationValue2 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue2.setType(ProductSpecificationValue.Type.TEXT.getValue());
        createSpecificationValues.add(createSpecificationValue2);
        createSpecification.setValues(createSpecificationValues);
        createSpecifications.add(createSpecification);
        // 图文类型商品规格
        CreateProductParam.CreateSpecification createSpecification1 = new CreateProductParam.CreateSpecification();
        createSpecification1.setName("颜色");
        List<CreateProductParam.CreateSpecificationValue> createSpecificationValues1 = new ArrayList<>();
        CreateProductParam.CreateSpecificationValue createSpecificationValue3 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue3.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        createSpecificationValue3.setValue("{\"name\" : \"黄色\", \"image\" : \"test\"}");
        createSpecificationValues1.add(createSpecificationValue3);
        CreateProductParam.CreateSpecificationValue createSpecificationValue4 = new CreateProductParam.CreateSpecificationValue();
        createSpecificationValue4.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        createSpecificationValue4.setValue("{\"name\" : \"蓝色\", \"image\" : \"test2\"}");
        createSpecificationValues1.add(createSpecificationValue4);
        createSpecification1.setValues(createSpecificationValues1);
        createSpecifications.add(createSpecification1);
        createProductParam.setSpecifications(createSpecifications);
        // sku
        List<CreateProductParam.CreateSku> createSkus = new ArrayList<>();
        CreateProductParam.CreateSku createSku = new CreateProductParam.CreateSku();
        createSku.setSkuCode("skuCode1");
        createSku.setStock(10000);
        createSku.setLowStock(20);
        createSku.setPrice(new BigDecimal("99.99"));
        createSku.setPic("图片");
        List<CreateProductParam.CreateSkuImage> createSkuImages = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(5) + 1; i++) {
            CreateProductParam.CreateSkuImage createSkuImage = new CreateProductParam.CreateSkuImage();
            createSkuImage.setImage("for test: " + i);
            createSkuImages.add(createSkuImage);
        }
        createSku.setImages(createSkuImages);
        // 大小：一般大，颜色：黄色
        createSku.setSpecificationIndex(new ArrayList<>(Arrays.asList(0, 0)));
        createSkus.add(createSku);
        CreateProductParam.CreateSku createSku1 = new CreateProductParam.CreateSku();
        BeanUtils.copyProperties(createSku, createSku1);
        // 大小：一般大，颜色：蓝色
        createSku1.setSpecificationIndex(new ArrayList<>(Arrays.asList(0, 1)));
        createSku1.setSkuCode("skuCode1");
        createSkus.add(createSku1);
        CreateProductParam.CreateSku createSku2 = new CreateProductParam.CreateSku();
        BeanUtils.copyProperties(createSku, createSku2);
        // 大小：还挺大，颜色：黄色
        createSku2.setSpecificationIndex(new ArrayList<>(Arrays.asList(1, 0)));
        createSku2.setSkuCode("skuCode2");
        createSkus.add(createSku2);
        createProductParam.setSkus(createSkus);
        List<Product> oldProducts = productMapper.selectAll();
        productService.create(createProductParam);
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
                break;
            }
        }
        assertNotNull(product);
        log.debug("product: " + product);
        List<Sku> skus = skuMapper.selectByProductId(product.getId());
        assertEquals(createSkus.size(), skus.size());
        log.debug("skus: " + skus);
        for (Sku sku : skus) {
            CreateProductParam.CreateSku foundSku = null;
            for (CreateProductParam.CreateSku cmpSku : createSkus) {
                if (sku.getSkuCode().equals(cmpSku.getSkuCode())) {
                    foundSku = cmpSku;
                    break;
                }
            }
            assertNotNull(foundSku);
            Sku copySku = new Sku();
            BeanUtils.copyProperties(sku, copySku);
            BeanUtils.copyProperties(foundSku, copySku);
            assertEquals(copySku, sku);
            List<SkuImage> skuImages = skuImageMapper.selectBySkuId(sku.getId());
            assertEquals(createSkuImages.size(), skuImages.size());
            log.debug("skuImages: " + skuImages);
            List<SkuSpecificationRelationship> relationships = skuSpecificationRelationshipMapper.selectBySkuId(sku.getId());
            assertEquals(createSpecifications.size(), relationships.size());
            log.debug("relationships: " + relationships);
        }
    }

    private CreateProductParam.CreateProduct getCreateProduct() {
        Brand brand = new Brand();
        brand.setName("test brand");
        brandMapper.insert(brand);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setParentId(ProductCategory.NON_PARENT_ID);
        productCategory.setName("test category");
        productCategoryMapper.insert(productCategory);
        CreateProductParam.CreateProduct createProduct = new CreateProductParam.CreateProduct();
        createProduct.setBrandId(brand.getId());
        createProduct.setProductCategoryId(productCategory.getId());
        createProduct.setProductSn("product sn");
        createProduct.setName("name");
        createProduct.setPic("pic");
        createProduct.setSubTitle("subTitle");
        createProduct.setDescription("description");
        createProduct.setDetailTitle("detailTitle");
        createProduct.setDetailDescription("detail description");
        createProduct.setDetailHtml("detail html");
        createProduct.setDetailMobileHtml("detail mobile html");
        createProduct.setPrice(new BigDecimal("99.99"));
        createProduct.setOriginalPrice(new BigDecimal("109.99"));
        createProduct.setStock(200);
        createProduct.setLowStock(20);
        createProduct.setUnit("个");
        createProduct.setNote("note");
        createProduct.setKeywords("keywords");
        createProduct.setSort(0);
        createProduct.setGiftPoint(30);
        createProduct.setUsePointLimit(100);
        createProduct.setServiceIds("1,2,3");
        createProduct.setPromotionType(Product.PromotionType.ORIGIN_PRICE.getValue());
        createProduct.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        createProduct.setIsNew(Product.NewStatus.NEW.getValue());
        createProduct.setIsRecommend(Product.RecommendStatus.RECOMMEND.getValue());
        createProduct.setIsPreview(Product.PreviewStatus.PREVIEW.getValue());
        List<CreateProductParam.CreateProductImage> createProductImages = new ArrayList<>();
        int size = new Random().nextInt(10);
        for (int i = 0; i < size; i++) {
            CreateProductParam.CreateProductImage image = new CreateProductParam.CreateProductImage();
            image.setImage("test");
            createProductImages.add(image);
        }
        createProduct.setImages(createProductImages);
        return createProduct;
    }

    @Test
    void getProductById() {
        // not exist
        ProductDTO productDTO1 = productService.getProductById(-1L);
        assertNull(productDTO1);

        // exist
        Product product = new Product();
        product.setName("test");
        productMapper.insert(product);
        ProductDTO productDTO = productService.getProductById(product.getId());
        assertNotNull(productDTO);
        log.debug("商品信息：" + productDTO);
    }

    @Test
    void updateProduct() {
        // 新增商品
        List<Product> oldProducts = productMapper.selectAll();
        testSaveCreateSkus();
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
            }
        }
        assertNotNull(product);
        Product createdProduct = product;

        // illegal
        // 商品不存在
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updateProduct(-1L, null));
        log.debug(throwable.getMessage());
        // 商品分类不存在
        UpdateProductParam param = new UpdateProductParam();
        param.setProductCategoryId(-1L);
        Throwable throwable1 = assertThrows(IllegalProductCategoryException.class, () -> productService.updateProduct(createdProduct.getId(), param));
        log.debug(throwable1.getMessage());
        // 商品品牌不存在
        UpdateProductParam param1 = new UpdateProductParam();
        param1.setBrandId(-1L);
        Throwable throwable2 = assertThrows(IllegalBrandException.class, () -> productService.updateProduct(createdProduct.getId(), param1));
        log.debug(throwable2.getMessage());

        // legal
        // 无商品图片
        UpdateProductParam param2 = new UpdateProductParam();
        Brand brand = new Brand();
        brand.setName("new brand");
        brandMapper.insert(brand);
        param2.setBrandId(brand.getId());
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("new category");
        productCategoryMapper.insert(productCategory);
        param2.setProductCategoryId(productCategory.getId());
        param2.setProductSn("update sn");
        param2.setName("update name");
        param2.setPic("update pic");
        param2.setSubTitle("update sub title");
        param2.setDescription("update description");
        param2.setDetailTitle("update detail title");
        param2.setDetailDescription("update detail description");
        param2.setDetailHtml("update detail html");
        param2.setDetailMobileHtml("update mobile html");
        param2.setPrice(new BigDecimal("299.99"));
        param2.setOriginalPrice(new BigDecimal("3999.99"));
        param2.setStock(999);
        param2.setLowStock(1);
        param2.setUnit("unit");
        param2.setNote("update note");
        param2.setKeywords("update keywords");
        param2.setSort(999);
        param2.setGiftPoint(999);
        param2.setUsePointLimit(2222);
        param2.setServiceIds("1,2,3");
        param2.setPromotionType(Product.PromotionType.MEMBER_PRICE.getValue());
        param2.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
        param2.setIsNew(Product.NewStatus.NOT_NEW.getValue());
        param2.setIsRecommend(Product.RecommendStatus.NOT_RECOMMEND.getValue());
        param2.setIsPreview(Product.PreviewStatus.PREVIEW.getValue());
        productService.updateProduct(createdProduct.getId(), param2);
        Product updateProduct = productMapper.selectById(createdProduct.getId());
        Product cmpProduct = new Product();
        BeanUtils.copyProperties(updateProduct, cmpProduct);
        BeanUtils.copyProperties(param2, cmpProduct);
        log.debug("updateProduct: " + updateProduct);
        assertEquals(cmpProduct, updateProduct);
        List<ProductImage> productImages = productImageMapper.selectByProductId(createdProduct.getId());
        assertEquals(0, productImages.size());
        // 有商品图片
        UpdateProductParam param3 = new UpdateProductParam();
        BeanUtils.copyProperties(param2, param3);
        List<UpdateProductParam.UpdateProductImage> updateProductImages = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            UpdateProductParam.UpdateProductImage updateProductImage = new UpdateProductParam.UpdateProductImage();
            updateProductImage.setImage("for test: " + i);
            updateProductImages.add(updateProductImage);
        }
        param3.setImages(updateProductImages);
        productService.updateProduct(createdProduct.getId(), param3);
        Product updateProduct1 = productMapper.selectById(createdProduct.getId());
        Product cmpProduct1 = new Product();
        BeanUtils.copyProperties(updateProduct1, cmpProduct1);
        BeanUtils.copyProperties(param3, cmpProduct1);
        log.debug("updateProduct1: " + updateProduct1);
        assertEquals(cmpProduct1, updateProduct1);
        List<ProductImage> productImages1 = productImageMapper.selectByProductId(createdProduct.getId());
        log.debug("productImages1: " + productImages1);
        assertEquals(updateProductImages.size(), productImages1.size());
    }

    @Test
    void updateParam() {
        List<Product> oldProducts = productMapper.selectAll();
        testSaveCreateSkus();
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
            }
        }
        assertNotNull(product);
        Random random = new Random();

        // 新增商品参数
        List<ProductParam> oldProductParams = productParamMapper.selectByProductId(product.getId());
        UpdateProductParamParam param = new UpdateProductParamParam();
        List<UpdateProductParamParam.UpdateProductParam> updateProductParams = oldProductParams.stream()
                .map(source -> {
                    UpdateProductParamParam.UpdateProductParam updateProductParam = new UpdateProductParamParam.UpdateProductParam();
                    updateProductParam.setId(source.getId());
                    return updateProductParam;
                }).collect(Collectors.toList());
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            UpdateProductParamParam.UpdateProductParam updateProductParam = new UpdateProductParamParam.UpdateProductParam();
            updateProductParam.setName("for test: " + i);
            updateProductParam.setType(ProductParam.Type.TEXT.getValue());
            updateProductParam.setValue("{\"name\" : \"test\"}");
            updateProductParams.add(updateProductParam);
        }
        param.setProductParams(updateProductParams);
        productService.updateParam(product.getId(), param);
        List<ProductParam> newProductParams = productParamMapper.selectByProductId(product.getId());
        log.debug("newProductParams: " + newProductParams);
        assertEquals(updateProductParams.size(), newProductParams.size());

        // 修改商品参数
        UpdateProductParamParam param1 = new UpdateProductParamParam();
        List<UpdateProductParamParam.UpdateProductParam> updateProductParams1 = new ArrayList<>();
        for (ProductParam productParam : newProductParams) {
            UpdateProductParamParam.UpdateProductParam updateProductParam = new UpdateProductParamParam.UpdateProductParam();
            updateProductParam.setId(productParam.getId());
            updateProductParam.setName("update name");
            updateProductParam.setType(ProductParam.Type.IMAGE_TEXT.getValue());
            updateProductParam.setValue("{\"name\" : \"update name\", \"image\" : \"image\"}");
            updateProductParams1.add(updateProductParam);
        }
        param1.setProductParams(updateProductParams1);
        productService.updateParam(product.getId(), param1);
        List<ProductParam> newProductParams1 = productParamMapper.selectByProductId(product.getId());
        log.debug("newProductParams1: " + newProductParams1);
        assertEquals(updateProductParams1.size(), newProductParams1.size());
        List<ProductParam> cmps = updateProductParams1.stream()
                .map(source -> {
                    ProductParam productParam = new ProductParam();
                    for (ProductParam newProductParam : newProductParams1) {
                        if (source.getId().equals(newProductParam.getId())) {
                            BeanUtils.copyProperties(newProductParam, productParam);
                        }
                    }
                    BeanUtils.copyProperties(source, productParam);
                    return productParam;
                }).collect(Collectors.toList());
        assertTrue(newProductParams1.containsAll(cmps));

        // 删除全部商品参数
        UpdateProductParamParam param2 = new UpdateProductParamParam();
        param2.setProductParams(new ArrayList<>());
        productService.updateParam(product.getId(), param2);
        List<ProductParam> newProductParams2 = productParamMapper.selectByProductId(product.getId());
        assertTrue(CollectionUtils.isEmpty(newProductParams2));
    }

    @Test
    void updateSpecification() {
        List<Product> oldProducts = productMapper.selectAll();
        testSaveCreateSkus();
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
            }
        }
        assertNotNull(product);
        Long productId = product.getId();
        ProductDTO productDTO = productService.getProductById(productId);
        List<ProductDTO.SpecificationDTO> specificationDTOList = productDTO.getSpecifications();

        // illegal
        // 商品不存在
        UpdateProductSpecificationParam param = new UpdateProductSpecificationParam();
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updateSpecification(-1L, param));
        log.debug(throwable.getMessage());
        // 商品规格不存在
        UpdateProductSpecificationParam param1 = new UpdateProductSpecificationParam();
        UpdateProductSpecificationParam.UpdateSpecification updateSpecification = new UpdateProductSpecificationParam.UpdateSpecification();
        updateSpecification.setValues(null);
        param1.setSpecifications(Collections.singletonList(updateSpecification));
        Throwable throwable1 = assertThrows(IllegalProductSpecificationException.class, () -> productService.updateSpecification(productId, param1));
        log.debug(throwable1.getMessage());

        // legal
        UpdateProductSpecificationParam param2 = new UpdateProductSpecificationParam();
        UpdateProductSpecificationParam.UpdateSpecification updateSpecification1 = new UpdateProductSpecificationParam.UpdateSpecification();
        updateSpecification1.setId(specificationDTOList.get(0).getId());
        List<UpdateProductSpecificationParam.UpdateSpecificationValue> updateSpecificationValues = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 10; i++) {
            UpdateProductSpecificationParam.UpdateSpecificationValue updateSpecificationValue = new UpdateProductSpecificationParam.UpdateSpecificationValue();
            updateSpecificationValue.setType(ProductSpecificationValue.Type.TEXT.getValue());
            updateSpecificationValue.setValue("{\"name\" : \"新选项\"}");
            updateSpecificationValues.add(updateSpecificationValue);
        }
        updateSpecification1.setValues(updateSpecificationValues);
        UpdateProductSpecificationParam.UpdateSpecification updateSpecification2 = new UpdateProductSpecificationParam.UpdateSpecification();
        updateSpecification2.setId(specificationDTOList.get(1).getId());
        updateSpecification2.setValues(new ArrayList<>());
        param2.setSpecifications(Arrays.asList(updateSpecification1, updateSpecification2));
        productService.updateSpecification(productId, param2);
        List<ProductDTO.SpecificationDTO> specificationDTOList1 = productService.getProductById(productId).getSpecifications();
        log.debug("specificationDTOList1: " + specificationDTOList1);
        assertEquals(specificationDTOList.get(0).getSpecificationValues().size() + updateSpecification1.getValues().size(),
                specificationDTOList1.get(0).getSpecificationValues().size());
        assertEquals(specificationDTOList.get(1).getSpecificationValues().size() + updateSpecification2.getValues().size(),
                specificationDTOList1.get(1).getSpecificationValues().size());
    }

    @Test
    void updateSku() {
        List<Product> oldProducts = productMapper.selectAll();
        testSaveCreateSkus();
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
            }
        }
        assertNotNull(product);
        Long productId = product.getId();
        ProductDTO productDTO = productService.getProductById(productId);
        List<ProductDTO.SpecificationDTO> specificationDTOS = productDTO.getSpecifications();
        List<ProductDTO.SkuDTO> skuDTOList = productDTO.getSkus();

        // illegal
        // 商品不存在
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updateSku(-1L, null));
        log.debug(throwable.getMessage());
        // sku商品规格为空
        UpdateSkuParam param = new UpdateSkuParam();
        UpdateSkuParam.UpdateSku updateSku = new UpdateSkuParam.UpdateSku();
        param.setSkus(Collections.singletonList(updateSku));
        Throwable throwable1 = assertThrows(IllegalProductSpecificationException.class, () -> productService.updateSku(productId, param));
        log.debug(throwable1.getMessage());
        // sku商品规格数量不合法
        UpdateSkuParam param1 = new UpdateSkuParam();
        UpdateSkuParam.UpdateSku updateSku1 = new UpdateSkuParam.UpdateSku();
        UpdateSkuParam.UpdateSkuSpecificationRelationship updateSkuSpecificationRelationship = new UpdateSkuParam.UpdateSkuSpecificationRelationship();
        updateSkuSpecificationRelationship.setSpecificationId(-1L);
        updateSku1.setSpecifications(Collections.singletonList(updateSkuSpecificationRelationship));
        param1.setSkus(Collections.singletonList(updateSku1));
        Throwable throwable2 = assertThrows(IllegalProductSpecificationException.class, () -> productService.updateSku(productId, param1));
        log.debug(throwable2.getMessage());
        // 缺少sku商品规格
        UpdateSkuParam param2 = new UpdateSkuParam();
        UpdateSkuParam.UpdateSku updateSku2 = new UpdateSkuParam.UpdateSku();
        List<UpdateSkuParam.UpdateSkuSpecificationRelationship> updateSkuSpecificationRelationships = new ArrayList<>();
        for (ProductDTO.SpecificationDTO specificationDTO : specificationDTOS) {
            UpdateSkuParam.UpdateSkuSpecificationRelationship updateSkuSpecificationRelationship1 = new UpdateSkuParam.UpdateSkuSpecificationRelationship();
            updateSkuSpecificationRelationship1.setSpecificationId(-1L);
            updateSkuSpecificationRelationship1.setSpecificationValueId(specificationDTO.getSpecificationValues().get(0).getId());
            updateSkuSpecificationRelationships.add(updateSkuSpecificationRelationship1);
        }
        updateSku2.setSpecifications(updateSkuSpecificationRelationships);
        param2.setSkus(Collections.singletonList(updateSku2));
        Throwable throwable3 = assertThrows(IllegalProductSpecificationException.class, () -> productService.updateSku(productId, param2));
        log.debug(throwable3.getMessage());
        // sku商品规格选项不存在
        UpdateSkuParam param3 = new UpdateSkuParam();
        UpdateSkuParam.UpdateSku updateSku3 = new UpdateSkuParam.UpdateSku();
        List<UpdateSkuParam.UpdateSkuSpecificationRelationship> updateSkuSpecificationRelationships1 = new ArrayList<>();
        for (ProductDTO.SpecificationDTO specificationDTO : specificationDTOS) {
            UpdateSkuParam.UpdateSkuSpecificationRelationship updateSkuSpecificationRelationship1 = new UpdateSkuParam.UpdateSkuSpecificationRelationship();
            updateSkuSpecificationRelationship1.setSpecificationId(specificationDTO.getId());
            updateSkuSpecificationRelationship1.setSpecificationValueId(-1L);
            updateSkuSpecificationRelationships1.add(updateSkuSpecificationRelationship1);
        }
        updateSku3.setSpecifications(updateSkuSpecificationRelationships1);
        param3.setSkus(Collections.singletonList(updateSku3));
        Throwable throwable4 = assertThrows(IllegalProductSpecificationException.class, () -> productService.updateSku(productId, param3));
        log.debug(throwable4.getMessage());

        // legal
        UpdateSkuParam param4 = new UpdateSkuParam();
        List<UpdateSkuParam.UpdateSku> updateSkus = new ArrayList<>();
        // 更新的sku
        Random random = new Random();
        UpdateSkuParam.UpdateSku updateSku4 = new UpdateSkuParam.UpdateSku();
        ProductDTO.SkuDTO skuDTO = skuDTOList.get(0);
        updateSku4.setId(skuDTO.getId());
        updateSku4.setLowStock(666);
        updateSku4.setPic("update pic");
        updateSku4.setPrice(new BigDecimal("666.66"));
        updateSku4.setSkuCode("update sku code");
        updateSku4.setStock(6666);
        List<UpdateSkuParam.UpdateSkuImage> updateSkuImages = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            UpdateSkuParam.UpdateSkuImage updateSkuImage = new UpdateSkuParam.UpdateSkuImage();
            updateSkuImage.setImage("update image: " + i);
            updateSkuImages.add(updateSkuImage);
        }
        updateSku4.setImages(updateSkuImages);
        updateSkus.add(updateSku4);
        // 新增的sku
        UpdateSkuParam.UpdateSku updateSku5 = new UpdateSkuParam.UpdateSku();
        updateSku5.setLowStock(666);
        updateSku5.setPic("add pic");
        updateSku5.setPrice(new BigDecimal("666.66"));
        updateSku5.setStock(6666);
        List<UpdateSkuParam.UpdateSkuImage> updateSkuImages1 = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            UpdateSkuParam.UpdateSkuImage updateSkuImage = new UpdateSkuParam.UpdateSkuImage();
            updateSkuImage.setImage("update image: " + i);
            updateSkuImages1.add(updateSkuImage);
        }
        updateSku5.setImages(updateSkuImages1);
        List<UpdateSkuParam.UpdateSkuSpecificationRelationship> updateSkuSpecificationRelationships2 = new ArrayList<>();
        for (ProductDTO.SpecificationDTO specificationDTO : specificationDTOS) {
            UpdateSkuParam.UpdateSkuSpecificationRelationship updateSkuSpecificationRelationship1 = new UpdateSkuParam.UpdateSkuSpecificationRelationship();
            updateSkuSpecificationRelationship1.setSpecificationId(specificationDTO.getId());
            updateSkuSpecificationRelationship1.setSpecificationValueId(specificationDTO.getSpecificationValues().get(0).getId());
            updateSkuSpecificationRelationships2.add(updateSkuSpecificationRelationship1);
        }
        updateSku5.setSpecifications(updateSkuSpecificationRelationships2);
        updateSkus.add(updateSku5);
        param4.setSkus(updateSkus);
        productService.updateSku(productId, param4);
        ProductDTO newProductDTO = productService.getProductById(productId);
        List<ProductDTO.SkuDTO> newSkuDTOList = newProductDTO.getSkus();
        log.debug("newSkuDTOList: " + newSkuDTOList);
        assertEquals(skuDTOList.size() + 1, newSkuDTOList.size());
        ProductDTO.SkuDTO addSkuDTO = null;
        for (ProductDTO.SkuDTO skuDTO1 : newSkuDTOList) {
            // 更新的sku
            if (skuDTO1.getId().equals(updateSku4.getId())) {
                ProductDTO.SkuDTO cmpSkuDTO = new ProductDTO.SkuDTO();
                BeanUtils.copyProperties(skuDTO1, cmpSkuDTO);
                BeanUtils.copyProperties(updateSku4, cmpSkuDTO);
                log.debug("update sku: " + skuDTO1);
                assertEquals(cmpSkuDTO, skuDTO1);
                assertEquals(updateSku4.getImages().size(), skuDTO1.getSkuImages().size());
            }
            // 新增的sku
            if (!skuDTOList.stream().map(Base::getId).collect(Collectors.toList()).contains(skuDTO1.getId())) {
                addSkuDTO = skuDTO1;
            }
        }
        log.debug("addSkuDTO: " + addSkuDTO);
        assertNotNull(addSkuDTO);
        assertEquals(updateSku5.getImages().size(), addSkuDTO.getSkuImages().size());
    }

    @Test
    void updatePublishStatus() {
        Random random = new Random();
        List<Product> oldProducts = productMapper.selectAll();
        int size = random.nextInt(20) + 10;
        for (int i = 0; i < size; i++) {
            testSaveCreateProduct();
        }
        List<Product> newProducts = productMapper.selectAll();
        List<Product> products = new ArrayList<>();
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                products.add(newProduct);
            }
        }
        assertEquals(size, products.size());

        // illegal
        List<Long> ids = products.stream().map(Base::getId).collect(Collectors.toList());
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updatePublishStatus(ids, -1));
        log.debug(throwable.getMessage());

        // legal
        productService.updatePublishStatus(ids, Product.PublishStatus.PUBLISH.getValue());
        List<Product> updateProducts = ids.stream().map(productMapper::selectById).collect(Collectors.toList());
        for (Product product : updateProducts) {
            assertEquals(Product.PublishStatus.PUBLISH.getValue(), product.getIsPublish());
        }
    }

    @Test
    void updateProductBatch() {
        Random random = new Random();
        List<Product> oldProducts = productMapper.selectAll();
        int size = random.nextInt(20) + 10;
        for (int i = 0; i < size; i++) {
            testSaveCreateProduct();
        }
        List<Product> newProducts = productMapper.selectAll();
        List<Product> products = new ArrayList<>();
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                products.add(newProduct);
            }
        }
        assertEquals(size, products.size());

        List<Long> ids = products.stream().map(Base::getId).collect(Collectors.toList());

        // 新的商品品牌
        Brand brand = new Brand();
        brand.setName("更新商品品牌");
        brandMapper.insert(brand);
        // 新的商品分类
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("更新商品分类");
        productCategoryMapper.insert(productCategory);

        UpdateProductBatchParam param = new UpdateProductBatchParam();
        param.setBrandId(brand.getId());
        param.setProductCategoryId(productCategory.getId());
        param.setProductSn("更新商品货号");
        param.setName("更新商品名称");
        param.setPic("更新图片");
        param.setSubTitle("更新副标题");
        param.setDescription("更新描述");
        param.setDetailTitle("更新标题");
        param.setDetailDescription("更新描述");
        param.setDetailHtml("update html");
        param.setDetailMobileHtml("update mobile html");
        param.setPrice(new BigDecimal("66.66"));
        param.setOriginalPrice(new BigDecimal("666.66"));
        param.setStock(20000);
        param.setLowStock(1);
        param.setUnit("更新单位");
        param.setNote("update note");
        param.setKeywords("更新关键词");
        param.setSort(1000);
        param.setGiftPoint(250);
        param.setUsePointLimit(250);
        param.setServiceIds("1,2,3,4,5");
        param.setPromotionType(Product.PromotionType.LADDER_PRICE.getValue());
        param.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        param.setIsNew(Product.NewStatus.NEW.getValue());
        param.setIsRecommend(Product.RecommendStatus.RECOMMEND.getValue());
        param.setIsPreview(Product.PreviewStatus.PREVIEW.getValue());
        productService.updateProductBatch(ids, param);
        List<Product> updateProducts = ids.stream().map(productMapper::selectById).collect(Collectors.toList());
        log.debug("updateProducts: " + updateProducts);
        for (Product product : updateProducts) {
            Product cmpProduct = new Product();
            BeanUtils.copyProperties(product, cmpProduct);
            BeanUtils.copyProperties(param, cmpProduct);
            assertEquals(cmpProduct, product);
        }
    }

    @Test
    void updateRecommendStatus() {
        Random random = new Random();
        List<Product> oldProducts = productMapper.selectAll();
        int size = random.nextInt(20) + 10;
        for (int i = 0; i < size; i++) {
            testSaveCreateProduct();
        }
        List<Product> newProducts = productMapper.selectAll();
        List<Product> products = new ArrayList<>();
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                products.add(newProduct);
            }
        }
        assertEquals(size, products.size());

        // illegal
        List<Long> ids = products.stream().map(Base::getId).collect(Collectors.toList());
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updateRecommendStatus(ids, -1));
        log.debug(throwable.getMessage());

        // legal
        productService.updateRecommendStatus(ids, Product.RecommendStatus.RECOMMEND.getValue());
        List<Product> updateProducts = ids.stream().map(productMapper::selectById).collect(Collectors.toList());
        for (Product product : updateProducts) {
            assertEquals(Product.RecommendStatus.RECOMMEND.getValue(), product.getIsRecommend());
        }
    }

    @Test
    void updateNewStatus() {
        Random random = new Random();
        List<Product> oldProducts = productMapper.selectAll();
        int size = random.nextInt(20) + 10;
        for (int i = 0; i < size; i++) {
            testSaveCreateProduct();
        }
        List<Product> newProducts = productMapper.selectAll();
        List<Product> products = new ArrayList<>();
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                products.add(newProduct);
            }
        }
        assertEquals(size, products.size());

        // illegal
        List<Long> ids = products.stream().map(Base::getId).collect(Collectors.toList());
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.updateNewStatus(ids, -1));
        log.debug(throwable.getMessage());

        // legal
        productService.updateNewStatus(ids, Product.NewStatus.NEW.getValue());
        List<Product> updateProducts = ids.stream().map(productMapper::selectById).collect(Collectors.toList());
        for (Product product : updateProducts) {
            assertEquals(Product.NewStatus.NEW.getValue(), product.getIsRecommend());
        }
    }

    @Test
    void querySku() {
        Random random = new Random();
        Long productId = 1234567L;
        List<Sku> skus = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 10; i++) {
            Sku sku = new Sku();
            sku.setSkuCode("for test: " + i);
            sku.setProductId(productId);
            skus.add(sku);
        }
        skus.forEach(skuMapper::insert);

        // empty
        List<Sku> skus1 = productService.querySku(-1L, "");
        assertEquals(0, skus1.size());
        List<Sku> skus3 = productService.querySku(productId, "not exist sku code");
        assertEquals(0, skus3.size());

        // not empty
        List<Sku> skus2 = productService.querySku(productId, "for test");
        log.debug("skus2: " + skus2);
        assertEquals(skus.size(), skus2.size());
        List<Sku> skus4 = productService.querySku(productId, "");
        log.debug("skus4: " + skus4);
        assertEquals(skus.size(), skus4.size());
    }

    @Test
    void verifyProduct() {
        List<Product> oldProducts = productMapper.selectAll();
        testSaveCreateSkus();
        List<Product> newProducts = productMapper.selectAll();
        Product product = null;
        for (Product newProduct : newProducts) {
            if (!oldProducts.contains(newProduct)) {
                product = newProduct;
            }
        }
        assertNotNull(product);
        Long id = product.getId();

        // illegal
        // 商品不存在
        Throwable throwable = assertThrows(IllegalProductException.class, () -> productService.verifyProduct(null, Product.VerifyStatus.VERIFY.getValue(), ""));
        log.debug("商品不存在：" + throwable);
        // 审核状态不合法
        Product updateProduct = new Product();
        updateProduct.setId(id);
        updateProduct.setIsVerify(Product.VerifyStatus.VERIFY.getValue());
        productMapper.updateById(updateProduct);
        Throwable throwable1 = assertThrows(IllegalProductException.class, () -> productService.verifyProduct(id, -1, ""));
        log.debug("审核状态不合法：" + throwable1);

        // legal
        Integer verifyStatus = Product.VerifyStatus.VERIFY.getValue();
        Product updateProduct1 = new Product();
        updateProduct1.setId(id);
        updateProduct1.setIsVerify(Product.VerifyStatus.NOT_VERIFY.getValue());
        productMapper.updateById(updateProduct1);
        productService.verifyProduct(id, verifyStatus, "note");
        Product newProduct = productMapper.selectById(id);
        assertEquals(verifyStatus, newProduct.getIsVerify());
    }

    @Test
    void queryProduct() {
        Product product = new Product();
        product.setName("商品名称测试用");
        product.setProductSn("a product sn");
        product.setBrandId(123456L);
        product.setProductCategoryId(1234567L);
        product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        product.setIsNew(Product.NewStatus.NOT_NEW.getValue());
        product.setIsRecommend(Product.RecommendStatus.RECOMMEND.getValue());
        product.setIsVerify(Product.VerifyStatus.VERIFY.getValue());
        product.setIsPreview(Product.PreviewStatus.NOT_PREVIEW.getValue());
        productMapper.insert(product);
        product = productMapper.selectById(product.getId());

        // not exist
        QueryProductParam param = new QueryProductParam();
        param.setName("123456不存在的商品名称");
        CommonPage<Product> result = productService.queryProduct(param, 1L, 10L);
        assertEquals(0, result.getData().size());

        QueryProductParam param1 = new QueryProductParam();
        param1.setProductSn("123456不存在的商品货号");
        CommonPage<Product> result1 = productService.queryProduct(param1, 1L, 10L);
        assertEquals(0, result1.getData().size());

        QueryProductParam param2 = new QueryProductParam();
        param2.setIsPublish(-1);
        CommonPage<Product> result2 = productService.queryProduct(param2, 1L, 10L);
        assertEquals(0, result2.getData().size());

        // exist
        QueryProductParam param3 = new QueryProductParam();
        param3.setName(product.getName());
        CommonPage<Product> result3 = productService.queryProduct(param3, 1L, 10L);
        assertEquals(1, result3.getData().size());

        QueryProductParam param4 = new QueryProductParam();
        param4.setProductSn(product.getProductSn());
        CommonPage<Product> result4 = productService.queryProduct(param4, 1L, 10L);
        assertEquals(1, result4.getData().size());

        QueryProductParam param5 = new QueryProductParam();
        BeanUtils.copyProperties(product, param5);
        CommonPage<Product> result5 = productService.queryProduct(param5, 1L, 10L);
        assertEquals(1, result5.getData().size());
    }
}