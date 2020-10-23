package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.portal.TestBase;
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
}