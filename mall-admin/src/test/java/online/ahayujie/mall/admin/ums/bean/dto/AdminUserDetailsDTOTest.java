package online.ahayujie.mall.admin.ums.bean.dto;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AdminUserDetailsDTOTest {

    @Test
    void getAuthorities() {
        List<Resource> resourceList = null;
        // null测试
        Collection<? extends GrantedAuthority> result1 = AdminUserDetailsDTO.getAuthorities(resourceList);
        assertEquals(result1.size(), 0);
        log.debug(result1.toString());

        // 空测试
        resourceList = new ArrayList<>();
        Collection<? extends GrantedAuthority> result2 = AdminUserDetailsDTO.getAuthorities(resourceList);
        assertEquals(result2.size(), 0);
        log.debug(result2.toString());

        // 正常测试
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20); i++) {
            Resource resource = new Resource();
            resource.setId(random.nextLong());
            resource.setName(getRandomString(random.nextInt(20)));
            resourceList.add(resource);
        }
        Collection<? extends GrantedAuthority> result3 = AdminUserDetailsDTO.getAuthorities(resourceList);
        assertEquals(result3.size(), resourceList.size());
        log.debug(resourceList.toString());
        log.debug(result3.toString());
    }

    @Test
    void testGetAuthorities() {
        String authorities = null;
        // null测试
        Collection<GrantedAuthority> result1 = AdminUserDetailsDTO.getAuthorities(authorities);
        assertEquals(result1.size(), 0);
        log.debug(result1.toString());

        // 空字符串测试
        authorities = "";
        Collection<GrantedAuthority> result2 = AdminUserDetailsDTO.getAuthorities(authorities);
        assertEquals(result2.size(), 0);
        log.debug(result2.toString());

        // 正常测试
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int size = random.nextInt(20) + 1;
        for (int i = 0; i < size; i++) {
            stringBuilder.append(random.nextLong()).append(":")
                    .append(getRandomString(random.nextInt(20))).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        authorities = stringBuilder.toString();
        Collection<GrantedAuthority> result3 = AdminUserDetailsDTO.getAuthorities(authorities);
        assertEquals(result3.size(), size);
        log.debug(authorities);
        log.debug(result3.toString());

        // 边界测试
        stringBuilder.append(",");
        authorities = stringBuilder.toString();
        Collection<GrantedAuthority> result4 = AdminUserDetailsDTO.getAuthorities(authorities);
        assertEquals(result4.size(), size);
        log.debug(authorities);
        log.debug(result4.toString());
    }

    @Test
    void getAuthoritiesString() {
        List<GrantedAuthority> authorityList = null;
        // null测试
        String result1 = AdminUserDetailsDTO.getAuthoritiesString(authorityList);
        assertEquals(result1, "");

        // 空测试
        authorityList = new ArrayList<>();
        String result2 = AdminUserDetailsDTO.getAuthoritiesString(authorityList);
        assertEquals(result2, "");

        // 正常测试
        Random random = new Random();
        for (int i = 0; i < random.nextInt(20); i++) {

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                    random.nextLong() + ":" +
                    getRandomString(random.nextInt(20) + 1)
            );
            authorityList.add(authority);
        }
        String result3 = AdminUserDetailsDTO.getAuthoritiesString(authorityList);
        assertNotNull(result3);
        assertEquals(result3.split(",").length, authorityList.size());
        log.debug(authorityList.toString());
        log.debug(result3);
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}