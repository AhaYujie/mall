package online.ahayujie.mall.admin.ums.bean.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aha
 * @date 2020/6/8
 */
@Slf4j
@Data
public class AdminUserDetailsDTO implements UserDetails {
    private Admin admin;
    private List<Resource> resourceList;

    public AdminUserDetailsDTO(Admin admin, List<Resource> resourceList) {
        this.admin = admin;
        this.resourceList = resourceList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities(resourceList);
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Admin.ACTIVE_STATUS.equals(admin.getStatus());
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(List<Resource> resourceList) {
        if (CollectionUtils.isEmpty(resourceList)) {
            return new ArrayList<>();
        }
        return resourceList.stream()
                .map(role ->new SimpleGrantedAuthority(role.getId() + ":" + role.getName()))
                .collect(Collectors.toList());
    }

    public static Collection<GrantedAuthority> getAuthorities(String authorities) {
        if (StringUtils.isEmpty(authorities)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(authorities.split(","))).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static String getAuthoritiesString(Collection<? extends GrantedAuthority> authorities) {
        if (CollectionUtils.isEmpty(authorities)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (GrantedAuthority authority : authorities) {
            stringBuilder.append(authority.getAuthority()).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }
}
