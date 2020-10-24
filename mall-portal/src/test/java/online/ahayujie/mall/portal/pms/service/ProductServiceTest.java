package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.dto.SkuDTO;
import online.ahayujie.mall.portal.pms.bean.model.*;
import online.ahayujie.mall.portal.pms.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductServiceTest extends TestBase {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
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
    private SkuSpecificationRelationshipMapper skuSpecificationRelationshipMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;

    @Test
    void getDetail() {
        Random random = new Random();
        Brand brand = new Brand();
        brand.setName(getRandomString(random.nextInt(5) + 5));
        brandMapper.insert(brand);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setParentId(ProductCategory.NON_PARENT_ID);
        productCategory.setName(getRandomString(random.nextInt(5) + 5));
        productCategoryMapper.insert(productCategory);
        Product product = new Product();
        product.setBrandId(brand.getId());
        product.setProductCategoryId(productCategory.getId());
        product.setName(getRandomString(random.nextInt(5) + 5));
        product.setDetailHtml(getRandomString(random.nextInt(50) + 100));
        product.setDetailMobileHtml(getRandomString(random.nextInt(50) + 100));
        product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        productMapper.insert(product);
        product = productMapper.selectById(product.getId());
        List<ProductImage> productImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(product.getId());
            productImage.setImage("http://" + getRandomString(random.nextInt(20) + 50) + ".jpg");
            productImageMapper.insert(productImage);
            productImage = productImageMapper.selectById(productImage.getId());
            productImages.add(productImage);
        }
        List<ProductParam> productParams = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ProductParam productParam = new ProductParam();
            productParam.setProductId(product.getId());
            productParam.setName(getRandomString(random.nextInt(3) + 2));
            productParam.setValue(getRandomString(random.nextInt(10) + 10));
            productParamMapper.insert(productParam);
            productParam = productParamMapper.selectById(productParam.getId());
            productParams.add(productParam);
        }
        Map<ProductSpecification, List<ProductSpecificationValue>> specificationMap = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            ProductSpecification specification = new ProductSpecification();
            specification.setProductId(product.getId());
            specification.setName(getRandomString(random.nextInt(2) + 2));
            productSpecificationMapper.insert(specification);
            specification = productSpecificationMapper.selectById(specification.getId());
            List<ProductSpecificationValue> values = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductSpecificationValue value = new ProductSpecificationValue();
                value.setProductSpecificationId(specification.getId());
                value.setValue(getRandomString(random.nextInt(10) + 10));
                productSpecificationValueMapper.insert(value);
                value = productSpecificationValueMapper.selectById(value.getId());
                values.add(value);
            }
            specificationMap.put(specification, values);
        }
        List<ProductSpecification> specifications = new ArrayList<>(specificationMap.keySet());
        List<Sku> skus = new ArrayList<>();
        Map<Sku, List<SkuSpecificationRelationship>> relationshipMap = new HashMap<>();
        for (int i = 0; i < specificationMap.get(specifications.get(0)).size(); i++) {
            for (int j = 0; j < specificationMap.get(specifications.get(1)).size(); j++) {
                Sku sku = new Sku();
                sku.setProductId(product.getId());
                skuMapper.insert(sku);
                sku = skuMapper.selectById(sku.getId());
                skus.add(sku);
                List<SkuSpecificationRelationship> relationships = new ArrayList<>();
                SkuSpecificationRelationship relationship = new SkuSpecificationRelationship();
                relationship.setSkuId(sku.getId());
                relationship.setSpecificationId(specifications.get(0).getId());
                relationship.setSpecificationValueId(specificationMap.get(specifications.get(0)).get(i).getId());
                skuSpecificationRelationshipMapper.insert(relationship);
                relationship = skuSpecificationRelationshipMapper.selectById(relationship.getId());
                SkuSpecificationRelationship relationship1 = new SkuSpecificationRelationship();
                relationship1.setSkuId(sku.getId());
                relationship1.setSpecificationId(specifications.get(1).getId());
                relationship1.setSpecificationValueId(specificationMap.get(specifications.get(1)).get(j).getId());
                skuSpecificationRelationshipMapper.insert(relationship1);
                relationship1 = skuSpecificationRelationshipMapper.selectById(relationship1.getId());
                relationships.add(relationship);
                relationships.add(relationship1);
                relationshipMap.put(sku, relationships);
            }
        }

        // 网页端
        ProductDetailDTO productDetailDTO = productService.getDetail(product.getId(), 0);
        Product compareProduct = new Product();
        BeanUtils.copyProperties(product, compareProduct);
        BeanUtils.copyProperties(productDetailDTO.getProductInfo(), compareProduct);
        compareProduct.setDetailMobileHtml(product.getDetailMobileHtml());
        assertEquals(product, compareProduct);
        assertNotNull(productDetailDTO.getProductInfo().getDetailHtml());
        assertNull(productDetailDTO.getProductInfo().getDetailMobileHtml());
        assertEquals(productImages.size(), productDetailDTO.getProductImages().size());
        List<String> compareProductImages = productImages.stream().map(ProductImage::getImage).collect(Collectors.toList());
        assertTrue(compareProductImages.containsAll(productDetailDTO.getProductImages()));
        assertEquals(productParams.size(), productDetailDTO.getParams().size());
        List<ProductDetailDTO.Param> compareParams = productParams.stream().map(source -> {
            ProductDetailDTO.Param target = new ProductDetailDTO.Param();
            BeanUtils.copyProperties(source, target);
            return target;
        }).collect(Collectors.toList());
        assertTrue(compareParams.containsAll(productDetailDTO.getParams()));
        assertEquals(specificationMap.keySet().size(), productDetailDTO.getSpecifications().size());
        for (ProductDetailDTO.Specification specificationDTO : productDetailDTO.getSpecifications()) {
            List<ProductSpecificationValue> values = null;
            for (ProductSpecification specification : specificationMap.keySet()) {
                if (specification.getId().equals(specificationDTO.getId())) {
                    values = specificationMap.get(specification);
                    break;
                }
            }
            assertNotNull(values);
            List<ProductDetailDTO.SpecificationValue> valueDTOS = values.stream().map(source -> {
                ProductDetailDTO.SpecificationValue target = new ProductDetailDTO.SpecificationValue();
                BeanUtils.copyProperties(source, target);
                return target;
            }).collect(Collectors.toList());
            assertEquals(valueDTOS.size(), specificationDTO.getValues().size());
            assertTrue(valueDTOS.containsAll(specificationDTO.getValues()));
        }
        assertEquals(skus.size(), productDetailDTO.getSkus().size());
        for (ProductDetailDTO.Sku skuDTO : productDetailDTO.getSkus()) {
            List<SkuSpecificationRelationship> relationships = null;
            for (Sku sku : relationshipMap.keySet()) {
                if (sku.getId().equals(skuDTO.getId())) {
                    relationships = relationshipMap.get(sku);
                    break;
                }
            }
            assertNotNull(relationships);
            List<ProductDetailDTO.SkuSpecificationRelationship> relationshipDTOS = relationships.stream().map(source -> {
                ProductDetailDTO.SkuSpecificationRelationship target = new ProductDetailDTO.SkuSpecificationRelationship();
                BeanUtils.copyProperties(source, target);
                return target;
            }).collect(Collectors.toList());
            assertEquals(relationshipDTOS.size(), skuDTO.getRelationships().size());
            assertTrue(relationshipDTOS.containsAll(skuDTO.getRelationships()));
        }

        // 移动端
        ProductDetailDTO productDetailDTO1 = productService.getDetail(product.getId(), 1);
        assertNull(productDetailDTO1.getProductInfo().getDetailHtml());
        assertNotNull(productDetailDTO1.getProductInfo().getDetailMobileHtml());

        // not exist
        ProductDetailDTO productDetailDTO2 = productService.getDetail(-1L, 0);
        assertNull(productDetailDTO2);
        Product update = new Product();
        update.setId(product.getId());
        update.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
        productMapper.updateById(update);
        ProductDetailDTO productDetailDTO3 = productService.getDetail(product.getId(), 0);
        assertNull(productDetailDTO3);
    }

    @Test
    void getSkuImages() {
        // exist
        Random random = new Random();
        Product product = new Product();
        product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        productMapper.insert(product);
        Sku sku = new Sku();
        sku.setProductId(product.getId());
        skuMapper.insert(sku);
        List<SkuImage> skuImages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SkuImage skuImage = new SkuImage();
            skuImage.setSkuId(sku.getId());
            skuImage.setImage("http://" + getRandomString(random.nextInt(20) + 50) + ".jpg");
            skuImageMapper.insert(skuImage);
            skuImages.add(skuImage);
        }
        List<String> images = productService.getSkuImages(sku.getId());
        List<String> compare = skuImages.stream().map(SkuImage::getImage).collect(Collectors.toList());
        assertEquals(compare.size(), images.size());
        assertTrue(compare.containsAll(images));

        // not exist
        List<String> images1 = productService.getSkuImages(-1L);
        assertNull(images1);
        product.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
        productMapper.updateById(product);
        List<String> images2 = productService.getSkuImages(sku.getId());
        assertNull(images2);
    }

    @Test
    void getIsPublish() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
            productMapper.insert(product);
            products.add(product);
        }
        List<Long> ids = products.stream().map(Base::getId).collect(Collectors.toList());
        ids.add(-1L);
        Map<Long, Integer> map = productService.getIsPublish(ids);
        for (Product product : products) {
            assertEquals(product.getIsPublish(), map.get(product.getId()));
        }
        assertFalse(map.containsKey(-1L));

        // null or empty
        assertNull(productService.getIsPublish(null));
        assertNull(productService.getIsPublish(Collections.emptyList()));
    }

    @Test
    void getSku() {
        // exist
        Random random = new Random();
        Product product = new Product();
        product.setIsPublish(Product.PublishStatus.PUBLISH.getValue());
        productMapper.insert(product);
        product = productMapper.selectById(product.getId());
        Map<ProductSpecification, List<ProductSpecificationValue>> specificationMap = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            ProductSpecification specification = new ProductSpecification();
            specification.setProductId(product.getId());
            specification.setName(getRandomString(random.nextInt(2) + 2));
            productSpecificationMapper.insert(specification);
            specification = productSpecificationMapper.selectById(specification.getId());
            List<ProductSpecificationValue> values = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ProductSpecificationValue value = new ProductSpecificationValue();
                value.setProductSpecificationId(specification.getId());
                value.setValue(getRandomString(random.nextInt(10) + 10));
                productSpecificationValueMapper.insert(value);
                value = productSpecificationValueMapper.selectById(value.getId());
                values.add(value);
            }
            specificationMap.put(specification, values);
        }
        List<ProductSpecification> specifications = new ArrayList<>(specificationMap.keySet());
        List<Sku> skus = new ArrayList<>();
        Map<Sku, List<SkuSpecificationRelationship>> relationshipMap = new HashMap<>();
        for (int i = 0; i < specificationMap.get(specifications.get(0)).size(); i++) {
            for (int j = 0; j < specificationMap.get(specifications.get(1)).size(); j++) {
                Sku sku = new Sku();
                sku.setProductId(product.getId());
                skuMapper.insert(sku);
                sku = skuMapper.selectById(sku.getId());
                skus.add(sku);
                List<SkuSpecificationRelationship> relationships = new ArrayList<>();
                SkuSpecificationRelationship relationship = new SkuSpecificationRelationship();
                relationship.setSkuId(sku.getId());
                relationship.setSpecificationId(specifications.get(0).getId());
                relationship.setSpecificationValueId(specificationMap.get(specifications.get(0)).get(i).getId());
                skuSpecificationRelationshipMapper.insert(relationship);
                relationship = skuSpecificationRelationshipMapper.selectById(relationship.getId());
                SkuSpecificationRelationship relationship1 = new SkuSpecificationRelationship();
                relationship1.setSkuId(sku.getId());
                relationship1.setSpecificationId(specifications.get(1).getId());
                relationship1.setSpecificationValueId(specificationMap.get(specifications.get(1)).get(j).getId());
                skuSpecificationRelationshipMapper.insert(relationship1);
                relationship1 = skuSpecificationRelationshipMapper.selectById(relationship1.getId());
                relationships.add(relationship);
                relationships.add(relationship1);
                relationshipMap.put(sku, relationships);
            }
        }

        SkuDTO result = productService.getSku(product.getId());
        assertEquals(specificationMap.keySet().size(), result.getSpecifications().size());
        for (ProductDetailDTO.Specification specificationDTO : result.getSpecifications()) {
            List<ProductSpecificationValue> values = null;
            for (ProductSpecification specification : specificationMap.keySet()) {
                if (specification.getId().equals(specificationDTO.getId())) {
                    values = specificationMap.get(specification);
                    break;
                }
            }
            assertNotNull(values);
            List<ProductDetailDTO.SpecificationValue> valueDTOS = values.stream().map(source -> {
                ProductDetailDTO.SpecificationValue target = new ProductDetailDTO.SpecificationValue();
                BeanUtils.copyProperties(source, target);
                return target;
            }).collect(Collectors.toList());
            assertEquals(valueDTOS.size(), specificationDTO.getValues().size());
            assertTrue(valueDTOS.containsAll(specificationDTO.getValues()));
        }
        assertEquals(skus.size(), result.getSkus().size());
        for (ProductDetailDTO.Sku skuDTO : result.getSkus()) {
            List<SkuSpecificationRelationship> relationships = null;
            for (Sku sku : relationshipMap.keySet()) {
                if (sku.getId().equals(skuDTO.getId())) {
                    relationships = relationshipMap.get(sku);
                    break;
                }
            }
            assertNotNull(relationships);
            List<ProductDetailDTO.SkuSpecificationRelationship> relationshipDTOS = relationships.stream().map(source -> {
                ProductDetailDTO.SkuSpecificationRelationship target = new ProductDetailDTO.SkuSpecificationRelationship();
                BeanUtils.copyProperties(source, target);
                return target;
            }).collect(Collectors.toList());
            assertEquals(relationshipDTOS.size(), skuDTO.getRelationships().size());
            assertTrue(relationshipDTOS.containsAll(skuDTO.getRelationships()));
        }

        // null
        assertNull(productService.getSku(-1L));
        Product update = new Product();
        update.setId(product.getId());
        update.setIsPublish(Product.PublishStatus.NOT_PUBLISH.getValue());
        productMapper.updateById(update);
        assertNull(productService.getSku(product.getId()));
    }
}