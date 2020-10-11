package online.ahayujie.mall.portal;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginDTO;
import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginParam;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import org.junit.BeforeClass;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author aha
 * @since 2020/10/9
 */
@Slf4j
public class TestBase {
    protected Member member;

    protected void initMember(PasswordEncoder passwordEncoder, MemberMapper memberMapper, MemberService memberService,
                              String header, String headerPrefix) {
        Random random = new Random();
        String password = "123456";
        member = new Member();
        member.setUsername(getRandomString(random.nextInt(10) + 6));
        member.setPassword(passwordEncoder.encode(password));
        member.setNickname(getRandomString(8));
        member.setPhone(getRandomNum(11));
        member.setIcon("http://" + getRandomString(50) + ".jpg");
        member.setBirthday(new Date());
        memberMapper.insert(member);
        member.setPassword(password);
        MemberLoginParam param = new MemberLoginParam();
        param.setUsername(member.getUsername());
        param.setPassword(member.getPassword());
        MemberLoginDTO memberLoginDTO = memberService.login(param);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assertNotNull(servletRequestAttributes);
        HttpServletRequest request = servletRequestAttributes.getRequest();
        MockHttpServletRequest mockHttpServletRequest = (MockHttpServletRequest) request;
        mockHttpServletRequest.addHeader(header, headerPrefix + memberLoginDTO.getAccessToken());
    }

    protected static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    protected static String getRandomNum(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(str.length()));
        }
        return stringBuilder.toString();
    }
}
