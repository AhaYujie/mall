package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.bean.model.ProductVerifyLog;
import online.ahayujie.mall.admin.pms.mapper.ProductVerifyLogMapper;
import online.ahayujie.mall.admin.pms.service.ProductVerifyLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductVerifyLogServiceImplTest {
    @Autowired
    private ProductVerifyLogService productVerifyLogService;
    @Autowired
    private ProductVerifyLogMapper productVerifyLogMapper;

    @Test
    void saveLog() {
        Long productId = 123456L;
        String note = "note";
        Integer isVerify = Product.VerifyStatus.VERIFY.getValue();
        List<ProductVerifyLog> oldLogs = productVerifyLogMapper.selectAll();
        productVerifyLogService.saveLog(productId, note, isVerify);
        List<ProductVerifyLog> newLogs = productVerifyLogMapper.selectAll();
        ProductVerifyLog productVerifyLog = null;
        for (ProductVerifyLog newLog : newLogs) {
            if (!oldLogs.contains(newLog)) {
                productVerifyLog = newLog;
            }
        }
        assertNotNull(productVerifyLog);
        log.debug("审核商品记录：" + productVerifyLog);
        assertEquals(productId, productVerifyLog.getProductId());
        assertEquals(note, productVerifyLog.getNote());
        assertEquals(isVerify, productVerifyLog.getIsVerify());
    }
}