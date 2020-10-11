package online.ahayujie.mall.portal.mms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.bean.dto.*;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest extends TestBase {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.header}")
    private String JWT_HEADER;
    @Value("${jwt.header-prefix}")
    private String JWT_HEADER_PREFIX;

    @BeforeEach
    void setUp() {
        initMember(passwordEncoder, memberMapper, memberService, JWT_HEADER, JWT_HEADER_PREFIX);
    }

    @Test
    void register() {
        Random random = new Random();
        // legal
        List<Member> oldList = memberMapper.selectList(Wrappers.emptyWrapper());
        String username = getRandomString(random.nextInt(20) + 6);
        String phone = getRandomNum(11);
        MemberRegisterParam param = new MemberRegisterParam();
        param.setUsername(username);
        param.setPassword("123456");
        param.setNickname(getRandomString(10));
        param.setPhone(phone);
        param.setIcon(getRandomString(50));
        memberService.register(param);
        List<Member> newList = memberMapper.selectList(Wrappers.emptyWrapper());
        Member member = null;
        for (Member compare : newList) {
            if (!oldList.contains(compare)) {
                member = compare;
                break;
            }
        }
        assertNotNull(member);
        assertEquals(username, member.getUsername());
        assertEquals(phone, member.getPhone());

        // 用户名已存在
        MemberRegisterParam param1 = new MemberRegisterParam();
        param1.setUsername(username);
        param1.setPhone(getRandomNum(11));
        param1.setPassword("123456");
        assertThrows(DuplicateUsernameException.class, () -> memberService.register(param1));

        // 手机号已存在
        MemberRegisterParam param2 = new MemberRegisterParam();
        param2.setUsername(getRandomString(random.nextInt(20) + 6));
        param2.setPhone(phone);
        param2.setPassword("123456");
        assertThrows(DuplicatePhoneException.class, () -> memberService.register(param2));
    }

    @Test
    void login() {
        Random random = new Random();

        // legal
        String password = "123456";
        Member member = new Member();
        member.setUsername(getRandomString(random.nextInt(20) + 10));
        member.setPassword(passwordEncoder.encode(password));
        member.setPhone(getRandomNum(11));
        memberMapper.insert(member);
        MemberLoginParam param = new MemberLoginParam();
        param.setUsername(member.getUsername());
        param.setPassword(password);
        MemberLoginDTO loginDTO = memberService.login(param);
        log.debug(loginDTO.toString());

        // 用户不存在
        MemberLoginParam param1 = new MemberLoginParam();
        param1.setUsername(getRandomString(random.nextInt(20) + 10));
        param1.setPassword(password);
        assertThrows(UsernameNotFoundException.class, () -> memberService.login(param1));

        // 密码错误
        MemberLoginParam param2 = new MemberLoginParam();
        param2.setUsername(member.getUsername());
        param2.setPassword("1234567");
        assertThrows(BadCredentialsException.class, () -> memberService.login(param2));

        // 用户被禁用
        Member updateMember = new Member();
        updateMember.setId(member.getId());
        updateMember.setStatus(Member.Status.DISABLE.value());
        memberMapper.updateById(updateMember);
        MemberLoginParam param3 = new MemberLoginParam();
        param3.setUsername(member.getUsername());
        param3.setPassword(password);
        assertThrows(DisabledException.class, () -> memberService.login(param3));
    }

    @Test
    void refreshAccessToken() {
        Random random = new Random();

        // legal
        String password = "123456";
        Member member = new Member();
        member.setUsername(getRandomString(random.nextInt(20) + 10));
        member.setPassword(passwordEncoder.encode(password));
        member.setPhone(getRandomNum(11));
        memberMapper.insert(member);
        MemberLoginParam param = new MemberLoginParam();
        param.setUsername(member.getUsername());
        param.setPassword(password);
        MemberLoginDTO loginDTO = memberService.login(param);
        MemberLoginDTO loginDTO1 = memberService.refreshAccessToken(loginDTO.getRefreshToken());
        assertNotNull(loginDTO1.getAccessToken());
        assertEquals(loginDTO.getRefreshToken(), loginDTO1.getRefreshToken());

        // illegal
        assertThrows(IllegalArgumentException.class, () -> memberService.refreshAccessToken("illegal"));
    }

    @Test
    void getInfo() {
        MemberDTO memberDTO = memberService.getInfo();
        member = memberMapper.selectById(member.getId());
        assertEquals(member.getUsername(), memberDTO.getUsername());
        assertEquals(member.getNickname(), memberDTO.getNickname());
        assertEquals(member.getPhone(), memberDTO.getPhone());
        assertEquals(member.getIcon(), memberDTO.getIcon());
        assertEquals(member.getGender(), memberDTO.getGender());
        assertEquals(member.getBirthday(), memberDTO.getBirthday());
        assertEquals(member.getIntegration(), memberDTO.getIntegration());
    }

    @Test
    void updateInfo() {
        // legal
        UpdateMemberParam param = new UpdateMemberParam();
        param.setNickname(getRandomString(10));
        param.setIcon("http://" + getRandomString(50) + ".jpg");
        param.setBirthday(new Date());
        param.setGender(Member.Gender.UN_KNOW.value());
        memberService.updateInfo(param);
        member = memberMapper.selectById(member.getId());
        assertEquals(param.getNickname(), member.getNickname());
        assertEquals(param.getIcon(), member.getIcon());
        assertEquals(param.getGender(), member.getGender());

        // 性别不合法
        UpdateMemberParam param1 = new UpdateMemberParam();
        param1.setGender(-1);
        assertThrows(IllegalArgumentException.class, () -> memberService.updateInfo(param1));
    }
}