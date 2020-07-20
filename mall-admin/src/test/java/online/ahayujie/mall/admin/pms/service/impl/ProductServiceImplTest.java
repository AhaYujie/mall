package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.*;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;
import online.ahayujie.mall.admin.pms.exception.IllegalSkuException;
import online.ahayujie.mall.admin.pms.mapper.*;
import online.ahayujie.mall.admin.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
        createProduct.setGiftGrowth(20);
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
        ProductDTO productDTO = productService.getProductById(product.getId());
        log.debug("商品信息：" + productDTO);
    }
}