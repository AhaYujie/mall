package online.ahayujie.mall.admin.pms.mapper;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.bean.model.SkuImage;
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
class SkuMapperTest {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;

    @Test
    void selectDTOByProductId() {
        // not exist
        List<ProductDTO.SkuDTO> skuDTOList = skuMapper.selectDTOByProductId(-1L);
        assertTrue(CollectionUtils.isEmpty(skuDTOList));

        // exist
        Long productId = 123456L;
        Random random = new Random();
        List<Sku> skus = new ArrayList<>();
        Map<Long, List<SkuImage>> skuImageMap = new HashMap<>();
        for (int i = 0; i < random.nextInt(10) + 1; i++) {
            Sku sku = new Sku();
            sku.setProductId(productId);
            sku.setSkuCode("for test: " + i);
            skuMapper.insert(sku);
            List<SkuImage> skuImages = new ArrayList<>();
            for (int j = 0; j < random.nextInt(10) + 1; j++) {
                SkuImage skuImage = new SkuImage();
                skuImage.setSkuId(sku.getId());
                skuImage.setImage("for test: " + sku.getId());
                skuImages.add(skuImage);
            }
            skuImageMapper.insertList(skuImages);
            skus.add(sku);
            skuImageMap.put(sku.getId(), skuImages);
        }
        List<ProductDTO.SkuDTO> skuDTOList1 = skuMapper.selectDTOByProductId(productId);
        log.debug("skuDTOList1: " + skuDTOList1);
        assertEquals(skus.size(), skuDTOList1.size());
        for (ProductDTO.SkuDTO skuDTO : skuDTOList1) {
            log.debug("skuDTO: " + skuDTO);
            log.debug("skuDTOImages: " + skuDTO.getSkuImages());
            assertEquals(skuImageMap.get(skuDTO.getId()).size(), skuDTO.getSkuImages().size());
        }
    }
}