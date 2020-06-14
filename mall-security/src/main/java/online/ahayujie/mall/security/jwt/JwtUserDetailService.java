package online.ahayujie.mall.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

/**
 * jwt用户信息接口
 * 实现此接口来定制spring security获取用户密码的方式和如何通过jwt承载数据claims获取用户角色权限
 * @author aha
 * @date 2020/4/22
 */
public interface JwtUserDetailService extends UserDetailsService {

    /**
     * 通过Claims获取用户角色权限，
     * 获取的用户角色权限会被用来创建 {@code Authentication}
     * @param claims jwt承载数据
     * @return 用户角色权限
     */
    Collection<GrantedAuthority> getAuthorities(Claims claims);

}
