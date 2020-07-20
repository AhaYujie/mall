package online.ahayujie.mall.admin.pms.mapper;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductSpecificationMapperTest {
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationValueMapper productSpecificationValueMapper;

    @Test
    void selectDTOByProductId() {
        // not exist
        List<ProductDTO.SpecificationDTO> specificationDTOS = productSpecificationMapper.selectDTOByProductId(-1L);
        assertTrue(CollectionUtils.isEmpty(specificationDTOS));

        // exist
        Random random = new Random();
        Long productId = 123456L;
        List<ProductSpecification> specifications = new ArrayList<>();
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            ProductSpecification specification = new ProductSpecification();
            specification.setProductId(productId);
            specification.setName("for test: " + i);
            specifications.add(specification);
        }
        productSpecificationMapper.insertList(specifications);
        Map<Long, List<ProductSpecificationValue>> valueMap = new HashMap<>();
        for (ProductSpecification specification : specifications) {
            List<ProductSpecificationValue> specificationValues = new ArrayList<>();
            for (int i = 0; i < random.nextInt(10) + 1; i++) {
                ProductSpecificationValue specificationValue = new ProductSpecificationValue();
                specificationValue.setProductSpecificationId(specification.getId());
                specificationValue.setValue("for test: " + i);
                specificationValues.add(specificationValue);
            }
            productSpecificationValueMapper.insertList(specificationValues);
            valueMap.put(specification.getId(), specificationValues);
        }
        List<ProductDTO.SpecificationDTO> specificationDTOS1 = productSpecificationMapper.selectDTOByProductId(productId);
        log.debug("specifications: " + specifications);
        log.debug("specificationDTOS1: " + specificationDTOS1);
        assertEquals(specifications.size(), specificationDTOS1.size());
        for (ProductDTO.SpecificationDTO specificationDTO : specificationDTOS1) {
            List<ProductSpecificationValue> specificationValues = specificationDTO.getSpecificationValues();
            assertEquals(valueMap.get(specificationDTO.getId()).size(), specificationValues.size());
            log.debug("specification: " + specificationDTO);
            log.debug("specificationValues: " + specificationValues);
        }
    }
}