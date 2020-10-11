package online.ahayujie.mall.portal.mms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.bean.dto.CreateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.dto.ReceiveAddressDTO;
import online.ahayujie.mall.portal.mms.bean.dto.UpdateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.mapper.ReceiveAddressMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ReceiveAddressServiceTest extends TestBase {
    @Autowired
    private ReceiveAddressService receiveAddressService;
    @Autowired
    private ReceiveAddressMapper receiveAddressMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;
    @Value("${jwt.header}")
    private String JWT_HEADER;
    @Value("${jwt.header-prefix}")
    private String JWT_HEADER_PREFIX;

    @BeforeEach
    void setUp() {
        initMember(passwordEncoder, memberMapper, memberService, JWT_HEADER, JWT_HEADER_PREFIX);
    }

    @Test
    void list() {
        // 不存在
        CommonPage<ReceiveAddressDTO> page = receiveAddressService.list(1L, 20L);
        assertEquals(0, page.getData().size());

        // 存在
        for (int i = 0; i < 10; i++) {
            ReceiveAddress receiveAddress = new ReceiveAddress();
            receiveAddress.setMemberId(member.getId());
            receiveAddress.setName("for test: " + i);
            if (i == 0) {
                receiveAddress.setIsDefault(ReceiveAddress.DEFAULT);
            }
            receiveAddressMapper.insert(receiveAddress);
        }
        CommonPage<ReceiveAddressDTO> page1 = receiveAddressService.list(1L, 5L);
        assertEquals(5, page1.getData().size());
        Integer prev = page1.getData().get(0).getIsDefault();
        for (int i = 1; i < page1.getData().size(); i++) {
            ReceiveAddressDTO receiveAddressDTO = page1.getData().get(i);
            assertTrue(prev >= receiveAddressDTO.getIsDefault());
            prev = receiveAddressDTO.getIsDefault();
        }
        CommonPage<ReceiveAddressDTO> page2 = receiveAddressService.list(2L, 5L);
        assertEquals(5, page2.getData().size());
        Integer prev1 = page2.getData().get(0).getIsDefault();
        for (int i = 1; i < page2.getData().size(); i++) {
            ReceiveAddressDTO receiveAddressDTO = page2.getData().get(i);
            assertTrue(prev1 >= receiveAddressDTO.getIsDefault());
            prev1 = receiveAddressDTO.getIsDefault();
        }
    }

    @Test
    void create() {
        // 默认地址
        CreateReceiveAddressParam param = new CreateReceiveAddressParam();
        param.setName(getRandomString(3));
        param.setPhoneNumber(getRandomNum(11));
        param.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddressService.create(param);
        ReceiveAddress receiveAddress = receiveAddressMapper.selectDefaultByMemberId(member.getId(), ReceiveAddress.DEFAULT);
        assertEquals(param.getName(), receiveAddress.getName());
        assertEquals(param.getPhoneNumber(), receiveAddress.getPhoneNumber());
        assertEquals(ReceiveAddress.DEFAULT, receiveAddress.getIsDefault());
        CreateReceiveAddressParam param1 = new CreateReceiveAddressParam();
        param1.setName(getRandomString(3));
        param1.setPhoneNumber(getRandomNum(11));
        param1.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddressService.create(param1);
        ReceiveAddress receiveAddress1 = receiveAddressMapper.selectDefaultByMemberId(member.getId(), ReceiveAddress.DEFAULT);
        assertEquals(param1.getName(), receiveAddress1.getName());
        assertEquals(param1.getPhoneNumber(), receiveAddress1.getPhoneNumber());
        assertEquals(ReceiveAddress.DEFAULT, receiveAddress1.getIsDefault());

        // 非默认地址
        List<ReceiveAddress> oldList = receiveAddressMapper.selectList(Wrappers.emptyWrapper());
        CreateReceiveAddressParam param2 = new CreateReceiveAddressParam();
        param2.setName(getRandomString(3));
        param2.setPhoneNumber(getRandomNum(11));
        param2.setIsDefault(ReceiveAddress.NOT_DEFAULT);
        receiveAddressService.create(param2);
        List<ReceiveAddress> newList = receiveAddressMapper.selectList(Wrappers.emptyWrapper());
        ReceiveAddress create = null;
        for (ReceiveAddress compare : newList) {
            if (!oldList.contains(compare)) {
                create = compare;
                break;
            }
        }
        assertNotNull(create);
        assertEquals(param2.getName(), create.getName());
        assertEquals(param2.getPhoneNumber(), create.getPhoneNumber());
        assertEquals(ReceiveAddress.NOT_DEFAULT, create.getIsDefault());
    }

    @Test
    void update() {
        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setMemberId(member.getId());
        receiveAddress.setName(getRandomString(3));
        receiveAddress.setPhoneNumber(getRandomNum(11));
        receiveAddressMapper.insert(receiveAddress);

        // 不存在
        UpdateReceiveAddressParam param = new UpdateReceiveAddressParam();
        param.setId(-1L);
        assertThrows(IllegalArgumentException.class, () -> receiveAddressService.update(param));

        // 非默认
        UpdateReceiveAddressParam param1 = new UpdateReceiveAddressParam();
        param1.setId(receiveAddress.getId());
        param1.setName(getRandomString(3));
        param1.setDetailAddress(getRandomString(20));
        receiveAddressService.update(param1);
        receiveAddress = receiveAddressMapper.selectById(receiveAddress.getId());
        assertEquals(param1.getName(), receiveAddress.getName());
        assertEquals(param1.getDetailAddress(), receiveAddress.getDetailAddress());

        // 默认
        UpdateReceiveAddressParam param2 = new UpdateReceiveAddressParam();
        param2.setId(receiveAddress.getId());
        param2.setName(getRandomString(3));
        param2.setDetailAddress(getRandomString(20));
        param2.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddressService.update(param2);
        receiveAddress = receiveAddressMapper.selectById(receiveAddress.getId());
        assertEquals(param2.getName(), receiveAddress.getName());
        assertEquals(param2.getDetailAddress(), receiveAddress.getDetailAddress());
        assertEquals(param2.getIsDefault(), receiveAddress.getIsDefault());
    }

    @Test
    void getDefault() {
        // null
        ReceiveAddressDTO receiveAddressDTO = receiveAddressService.getDefault();
        assertNull(receiveAddressDTO);

        // not null
        ReceiveAddress receiveAddress = new ReceiveAddress();
        receiveAddress.setMemberId(member.getId());
        receiveAddress.setName(getRandomString(3));
        receiveAddress.setIsDefault(ReceiveAddress.DEFAULT);
        receiveAddressMapper.insert(receiveAddress);
        ReceiveAddressDTO receiveAddressDTO1 = receiveAddressService.getDefault();
        assertNotNull(receiveAddressDTO1);
        assertEquals(receiveAddress.getName(), receiveAddressDTO1.getName());
        assertEquals(ReceiveAddress.DEFAULT, receiveAddressDTO1.getIsDefault());
    }
}