package online.ahayujie.mall.admin.mms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.admin.mms.mapper.ReceiveAddressMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ReceiveAddressServiceTest {
    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;
    @Autowired
    private ReceiveAddressService receiveAddressService;

    @Test
    void getDefaultReceiveAddress() {
        // null
        ReceiveAddress receiveAddress = receiveAddressService.getDefaultReceiveAddress(-1L);
        assertNull(receiveAddress);
        Long memberId = 1234567L;
        receiveAddressMapper.deleteByMemberId(memberId);
        ReceiveAddress receiveAddress1 = new ReceiveAddress();
        receiveAddress1.setMemberId(memberId);
        receiveAddress1.setIsDefault(ReceiveAddress.NOT_DEFAULT);
        receiveAddressMapper.insert(receiveAddress1);
        ReceiveAddress receiveAddress2 = receiveAddressService.getDefaultReceiveAddress(memberId);
        assertNull(receiveAddress2);

        // not null
        ReceiveAddress receiveAddress3 = new ReceiveAddress();
        receiveAddress3.setMemberId(memberId);
        receiveAddress3.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddress3.setName("默认地址");
        receiveAddressMapper.insert(receiveAddress3);
        ReceiveAddress receiveAddress4 = receiveAddressService.getDefaultReceiveAddress(memberId);
        assertNotNull(receiveAddress4);
        assertEquals(receiveAddress3.getName(), receiveAddress4.getName());
    }
}