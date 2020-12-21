package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.oms.bean.dto.SubmitOrderParam;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import online.ahayujie.mall.portal.pms.mapper.SkuMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class SkuServiceTest extends TestBase {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuService skuService;

    @Test
    void getPrice() {
        List<Sku> skus = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Sku sku = new Sku();
            sku.setPrice(new BigDecimal(getRandomNum(2)));
            skuMapper.insert(sku);
            sku = skuMapper.selectById(sku.getId());
            skus.add(sku);
        }
        List<Long> ids = skus.stream().map(Base::getId).collect(Collectors.toList());
        ids.add(-1L);
        Map<Long, BigDecimal> map = skuService.getPrice(ids);
        for (Sku sku : skus) {
            assertEquals(sku.getPrice(), map.get(sku.getId()));
        }
        assertFalse(map.containsKey(-1L));

        // null or empty
        assertNull(skuService.getPrice(null));
        assertNull(skuService.getPrice(Collections.emptyList()));
    }

    @Test
    void updateStock() {
        List<Sku> skus = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Sku sku = new Sku();
            sku.setStock(i + 1);
            skuMapper.insert(sku);
            skus.add(sku);
        }

//        List<SubmitOrderParam.Product> products2 = new ArrayList<>();
//        for (Sku sku : skus) {
//            SubmitOrderParam.Product product = new SubmitOrderParam.Product();
//            product.setSkuId(sku.getId());
//            product.setQuantity(sku.getStock());
//            products2.add(product);
//        }
//        products2.get(products2.size() - 1).setQuantity(products2.get(products2.size() - 1).getQuantity() + 10);
//        assertThrows(IllegalArgumentException.class, () -> skuService.updateStock(products2));

        // fail
        List<SubmitOrderParam.Product> products = new ArrayList<>();
        SubmitOrderParam.Product product1 = new SubmitOrderParam.Product();
        product1.setSkuId(skus.get(0).getId());
        product1.setQuantity(skus.get(0).getStock() + 10);
        products.add(product1);
        assertThrows(IllegalArgumentException.class, () -> skuService.updateStock(products));

        // success
        List<SubmitOrderParam.Product> products1 = new ArrayList<>();
        for (Sku sku : skus) {
            SubmitOrderParam.Product product = new SubmitOrderParam.Product();
            product.setSkuId(sku.getId());
            product.setQuantity(sku.getStock());
            products1.add(product);
        }
        skuService.updateStock(products1);
        for (Sku sku : skus) {
            assertEquals(0, skuMapper.selectById(sku.getId()).getStock());
        }
    }
}