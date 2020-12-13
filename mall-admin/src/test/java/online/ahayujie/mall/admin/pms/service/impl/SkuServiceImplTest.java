package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.admin.pms.bean.model.*;
import online.ahayujie.mall.admin.pms.mapper.SkuImageMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuSpecificationRelationshipMapper;
import online.ahayujie.mall.admin.pms.service.SkuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class SkuServiceImplTest {
    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuSpecificationRelationshipMapper skuSpecificationRelationshipMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Test
    void generateSkuCode() {
        // NPE
        assertThrows(NullPointerException.class, () -> skuService.generateSkuCode(null, 1));
        assertThrows(NullPointerException.class, () -> skuService.generateSkuCode(new Sku(), 1));

        // legal
        Sku sku = new Sku();
        sku.setProductId(1L);
        String skuCode = skuService.generateSkuCode(sku, 111);
        log.debug("skuCode: " + skuCode);
        sku.setProductId(123456789L);
        skuCode = skuService.generateSkuCode(sku, 222);
        log.debug("skuCode: " + skuCode);
    }

    @Test
    void generateSpecification() {
        List<ProductSpecificationDTO> specificationDTOS = new ArrayList<>();
        ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setName("name");
        ProductSpecificationValue specificationValue = new ProductSpecificationValue();
        specificationValue.setType(ProductSpecificationValue.Type.TEXT.getValue());
        specificationValue.setValue("{\"name\" : \"aha\"}");
        ProductSpecificationDTO specificationDTO = new ProductSpecificationDTO();
        specificationDTO.setSpecification(productSpecification);
        specificationDTO.setSpecificationValue(specificationValue);
        specificationDTOS.add(specificationDTO);
        ProductSpecification productSpecification1 = new ProductSpecification();
        productSpecification1.setName("color");
        ProductSpecificationValue specificationValue1 = new ProductSpecificationValue();
        specificationValue1.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
        specificationValue1.setValue("{\"name\" : \"yellow\", \"image\" : \"yellow\"}");
        ProductSpecificationDTO specificationDTO1 = new ProductSpecificationDTO();
        specificationDTO1.setSpecification(productSpecification1);
        specificationDTO1.setSpecificationValue(specificationValue1);
        specificationDTOS.add(specificationDTO1);
        String specificationJson = skuService.generateSpecification(specificationDTOS);
        log.debug("specificationJson: " + specificationJson);
    }

    @Test
    void saveSkuImages() {
        List<SkuImage> skuImages = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            SkuImage skuImage = new SkuImage();
            skuImage.setImage("for test: " + i);
            skuImages.add(skuImage);
        }
        List<SkuImage> oldSkuImages = skuImageMapper.selectAll();
        skuService.saveSkuImages(skuImages);
        List<SkuImage> newSkuImages = skuImageMapper.selectAll();
        assertEquals(oldSkuImages.size() + skuImages.size(), newSkuImages.size());
    }

    @Test
    void saveSkuSpecificationRelationships() {
        List<SkuSpecificationRelationship> relationships = new ArrayList<>();
        for (int i = 0; i < new Random().nextInt(20) + 1; i++) {
            SkuSpecificationRelationship relationship = new SkuSpecificationRelationship();
            relationships.add(relationship);
        }
        List<SkuSpecificationRelationship> oldRelationships = skuSpecificationRelationshipMapper.selectAll();
        skuService.saveSkuSpecificationRelationships(relationships);
        List<SkuSpecificationRelationship> newRelationships = skuSpecificationRelationshipMapper.selectAll();
        assertEquals(oldRelationships.size() + relationships.size(), newRelationships.size());
    }

    @Test
    void getAllSkuSpecificationRelationships() {
        Long productId = 1L;
        List<Sku> skus = new ArrayList<>();
        Map<Sku, List<SkuSpecificationRelationship>> map = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            Sku sku = new Sku();
            sku.setProductId(productId);
            skuMapper.insert(sku);
            List<SkuSpecificationRelationship> relationships = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                SkuSpecificationRelationship relationship = new SkuSpecificationRelationship();
                relationship.setSkuId(sku.getId());
                relationship.setSpecificationId((long) j);
                relationship.setSpecificationValueId((long) j);
                skuSpecificationRelationshipMapper.insert(relationship);
                relationships.add(relationship);
            }
            skus.add(sku);
            map.put(sku, relationships);
        }

        List<List<SkuSpecificationRelationship>> result = skuService.getAllSkuSpecificationRelationships(productId);
        assertFalse(result.isEmpty());
        for (List<SkuSpecificationRelationship> sub : result) {
            assertFalse(sub.isEmpty());
            Long skuId = sub.get(0).getSkuId();
            Sku sku = null;
            for (Sku compare : skus) {
                if (compare.getId().equals(skuId)) {
                    sku = compare;
                    break;
                }
            }
            assertNotNull(sku);
            List<SkuSpecificationRelationship> compare = map.get(sku);
            assertTrue(sub.containsAll(compare));
        }
    }
}