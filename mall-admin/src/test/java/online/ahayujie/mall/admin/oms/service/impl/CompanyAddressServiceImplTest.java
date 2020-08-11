package online.ahayujie.mall.admin.oms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.oms.bean.dto.CreateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.admin.oms.mapper.CompanyAddressMapper;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class CompanyAddressServiceImplTest {
    @Autowired
    private CompanyAddressServiceImpl companyAddressService;
    @Autowired
    private CompanyAddressMapper companyAddressMapper;

    @Test
    void create() {
        // illegal
        // 非法默认发货地址状态
        CreateCompanyAddressParam param = new CreateCompanyAddressParam();
        param.setIsSendDefault(-1);
        Throwable throwable = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.create(param));
        log.debug(throwable.getMessage());
        // 非法默认收货地址状态
        CreateCompanyAddressParam param1 = new CreateCompanyAddressParam();
        param1.setIsReceiveDefault(-1);
        Throwable throwable1 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.create(param1));
        log.debug(throwable1.getMessage());
        // 非法启用状态
        CreateCompanyAddressParam param2 = new CreateCompanyAddressParam();
        param2.setStatus(-1);
        Throwable throwable2 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.create(param2));
        log.debug(throwable2.getMessage());
        // 已存在默认发货地址
        if (companyAddressMapper.selectSendDefault(CompanyAddress.SEND_DEFAULT) == null) {
            CompanyAddress companyAddress = new CompanyAddress();
            companyAddress.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
            companyAddressMapper.insert(companyAddress);
        }
        CreateCompanyAddressParam param4 = new CreateCompanyAddressParam();
        param4.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
        Throwable throwable3 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.create(param4));
        log.debug(throwable3.getMessage());
        // 已存在默认收货地址
        if (companyAddressMapper.selectReceiveDefault(CompanyAddress.RECEIVE_DEFAULT) == null) {
            CompanyAddress companyAddress = new CompanyAddress();
            companyAddress.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
            companyAddressMapper.insert(companyAddress);
        }
        CreateCompanyAddressParam param5 = new CreateCompanyAddressParam();
        param5.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        Throwable throwable4 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.create(param5));
        log.debug(throwable4.getMessage());

        // legal
        // 不是默认收发货地址
        CreateCompanyAddressParam param3 = new CreateCompanyAddressParam();
        param3.setName("name");
        param3.setReceiverName("收发货人姓名");
        param3.setReceiverPhone("收发货人电话");
        param3.setProvince("省/直辖市");
        param3.setCity("市");
        param3.setRegion("区");
        param3.setStreet("街道");
        param3.setDetailAddress("详细地址");
        param3.setIsSendDefault(CompanyAddress.NOT_SEND_DEFAULT);
        param3.setIsReceiveDefault(CompanyAddress.NOT_RECEIVE_DEFAULT);
        param3.setStatus(CompanyAddress.ACTIVE_STATUS);
        List<CompanyAddress> oldCompanyAddress = companyAddressMapper.selectAll();
        companyAddressService.create(param3);
        List<CompanyAddress> newCompanyAddress = companyAddressMapper.selectAll();
        assertEquals(oldCompanyAddress.size() + 1, newCompanyAddress.size());
        CompanyAddress companyAddress = findUnique(oldCompanyAddress, newCompanyAddress);
        assertNotNull(companyAddress);
        CreateCompanyAddressParam compare = new CreateCompanyAddressParam();
        BeanUtils.copyProperties(companyAddress, compare);
        assertEquals(param3, compare);
        log.debug("companyAddress: " + companyAddress);

        // 是默认收发货地址，且数据库原本没有默认收发货地址
        CompanyAddress defaultSend = companyAddressMapper.selectSendDefault(CompanyAddress.SEND_DEFAULT);
        if (defaultSend != null) {
            companyAddressMapper.deleteById(defaultSend.getId());
        }
        CompanyAddress defaultReceive = companyAddressMapper.selectReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        if (defaultReceive != null) {
            companyAddressMapper.deleteById(defaultReceive.getId());
        }
        CreateCompanyAddressParam param6 = new CreateCompanyAddressParam();
        BeanUtils.copyProperties(param3, param6);
        param6.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
        param6.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        List<CompanyAddress> oldCompanyAddress1 = companyAddressMapper.selectAll();
        companyAddressService.create(param6);
        List<CompanyAddress> newCompanyAddress1 = companyAddressMapper.selectAll();
        assertEquals(oldCompanyAddress1.size() + 1, newCompanyAddress1.size());
        CompanyAddress companyAddress1 = findUnique(oldCompanyAddress1, newCompanyAddress1);
        assertNotNull(companyAddress1);
        CreateCompanyAddressParam compare1 = new CreateCompanyAddressParam();
        BeanUtils.copyProperties(companyAddress1, compare1);
        assertEquals(param6, compare1);
        log.debug("companyAddress1: " + companyAddress1);
    }

    private CompanyAddress findUnique(List<CompanyAddress> oldCompanyAddress, List<CompanyAddress> newCompanyAddress) {
        for (CompanyAddress companyAddress : newCompanyAddress) {
            if (!oldCompanyAddress.contains(companyAddress)) {
                return companyAddress;
            }
        }
        return null;
    }

    @Test
    void update() {
        CompanyAddress companyAddress = new CompanyAddress();
        companyAddress.setName("for test");
        companyAddressMapper.insert(companyAddress);
        Long id = companyAddress.getId();

        // illegal
        // 非法默认发货地址状态
        UpdateCompanyAddressParam param = new UpdateCompanyAddressParam();
        param.setIsSendDefault(-1);
        Throwable throwable = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.update(id, param));
        log.debug(throwable.getMessage());
        // 非法默认收货地址状态
        UpdateCompanyAddressParam param1 = new UpdateCompanyAddressParam();
        param1.setIsReceiveDefault(-1);
        Throwable throwable1 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.update(id, param1));
        log.debug(throwable1.getMessage());
        // 非法启用状态
        UpdateCompanyAddressParam param2 = new UpdateCompanyAddressParam();
        param2.setStatus(-1);
        Throwable throwable2 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.update(id, param2));
        log.debug(throwable2.getMessage());
        // 已存在默认发货地址
        if (companyAddressMapper.selectSendDefault(CompanyAddress.SEND_DEFAULT) == null) {
            CompanyAddress companyAddress1 = new CompanyAddress();
            companyAddress1.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
            companyAddressMapper.insert(companyAddress1);
        }
        UpdateCompanyAddressParam param4 = new UpdateCompanyAddressParam();
        param4.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
        Throwable throwable3 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.update(id, param4));
        log.debug(throwable3.getMessage());
        // 已存在默认收货地址
        if (companyAddressMapper.selectReceiveDefault(CompanyAddress.RECEIVE_DEFAULT) == null) {
            CompanyAddress companyAddress1 = new CompanyAddress();
            companyAddress1.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
            companyAddressMapper.insert(companyAddress1);
        }
        UpdateCompanyAddressParam param5 = new UpdateCompanyAddressParam();
        param5.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        Throwable throwable4 = assertThrows(IllegalCompanyAddressException.class, () -> companyAddressService.update(id, param5));
        log.debug(throwable4.getMessage());

        // legal
        // 不是默认收发货地址
        UpdateCompanyAddressParam param3 = new UpdateCompanyAddressParam();
        param3.setName("name");
        param3.setReceiverName("收发货人姓名");
        param3.setReceiverPhone("收发货人电话");
        param3.setProvince("省/直辖市");
        param3.setCity("市");
        param3.setRegion("区");
        param3.setStreet("街道");
        param3.setDetailAddress("详细地址");
        param3.setIsSendDefault(CompanyAddress.NOT_SEND_DEFAULT);
        param3.setIsReceiveDefault(CompanyAddress.NOT_RECEIVE_DEFAULT);
        param3.setStatus(CompanyAddress.ACTIVE_STATUS);
        companyAddressService.update(id, param3);
        CompanyAddress companyAddress1 = companyAddressMapper.selectById(id);
        assertNotNull(companyAddress1);
        UpdateCompanyAddressParam compare = new UpdateCompanyAddressParam();
        BeanUtils.copyProperties(companyAddress1, compare);
        assertEquals(param3, compare);
        log.debug("companyAddress1: " + companyAddress1);

        // 是默认收发货地址，且数据库原本没有默认收发货地址
        CompanyAddress defaultSend = companyAddressMapper.selectSendDefault(CompanyAddress.SEND_DEFAULT);
        if (defaultSend != null) {
            companyAddressMapper.deleteById(defaultSend.getId());
        }
        CompanyAddress defaultReceive = companyAddressMapper.selectReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        if (defaultReceive != null) {
            companyAddressMapper.deleteById(defaultReceive.getId());
        }
        UpdateCompanyAddressParam param6 = new UpdateCompanyAddressParam();
        BeanUtils.copyProperties(param3, param6);
        param6.setIsSendDefault(CompanyAddress.SEND_DEFAULT);
        param6.setIsReceiveDefault(CompanyAddress.RECEIVE_DEFAULT);
        companyAddressService.update(id, param6);
        CompanyAddress companyAddress2 = companyAddressMapper.selectById(id);
        assertNotNull(companyAddress2);
        UpdateCompanyAddressParam compare1 = new UpdateCompanyAddressParam();
        BeanUtils.copyProperties(companyAddress2, compare1);
        assertEquals(param6, compare1);
        log.debug("companyAddress2: " + companyAddress2);
    }

    @Test
    void delete() {
        Random random = new Random();
        List<CompanyAddress> companyAddresses = new ArrayList<>();
        for (int i = 0; i < random.nextInt(20) + 5; i++) {
            CompanyAddress companyAddress = new CompanyAddress();
            companyAddress.setName("for test: " + i);
            companyAddresses.add(companyAddress);
        }
        companyAddresses.forEach(companyAddressMapper::insert);
        List<CompanyAddress> oldCompanyAddresses = companyAddressMapper.selectAll();
        List<Long> ids = companyAddresses.stream().map(Base::getId).collect(Collectors.toList());
        ids.forEach(companyAddressService::delete);
        List<CompanyAddress> newCompanyAddresses = companyAddressMapper.selectAll();
        assertEquals(oldCompanyAddresses.size() - ids.size(), newCompanyAddresses.size());
        for (Long id : ids) {
            assertNull(companyAddressMapper.selectById(id));
        }
    }

    @Test
    void list() {
        List<CompanyAddress> companyAddresses = companyAddressMapper.selectAll();
        companyAddresses.forEach(companyAddress -> companyAddressMapper.deleteById(companyAddress.getId()));

        int size = 10;
        List<CompanyAddress> companyAddresses1 = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CompanyAddress companyAddress = new CompanyAddress();
            companyAddress.setName("for test: " + i);
            companyAddresses1.add(companyAddress);
        }
        companyAddresses1.forEach(companyAddressMapper::insert);

        CommonPage<CompanyAddress> result = companyAddressService.list(1, 6);
        log.debug("result: " + result);
        assertEquals(6, result.getData().size());
        CommonPage<CompanyAddress> result1 = companyAddressService.list(2, 6);
        log.debug("result1: " + result1);
        assertEquals(4, result1.getData().size());
    }
}