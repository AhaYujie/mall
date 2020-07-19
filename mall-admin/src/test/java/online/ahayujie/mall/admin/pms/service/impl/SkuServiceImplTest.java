package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.admin.pms.bean.model.*;
import online.ahayujie.mall.admin.pms.mapper.SkuImageMapper;
import online.ahayujie.mall.admin.pms.mapper.SkuSpecificationRelationshipMapper;
import online.ahayujie.mall.admin.pms.service.SkuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
}