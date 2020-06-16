package online.ahayujie.mall.security.component;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * 动态权限 Service
 * @author aha
 * @date 2020/6/7
 */
public interface DynamicSecurityService {
    /**
     * 获取需要的权限
     * 此方法在 {@code FilterInvocationSecurityMetadataSource.getAttributes} 中
     * 被调用，然后在 {@code AccessDecisionManager.decide} 中被用来与
     * 从 {@code Collection<ConfigAttribute> getAttributes(String path)} 方法获取的
     * 用户权限集合进行比对
     * @param path 访问地址
     * @return 权限
     */
    Collection<ConfigAttribute> getAttributes(String path);

    /**
     * 获取用户拥有的权限
     * 此方法在 {@code AccessDecisionManager.decide} 中被调用，返回结果被用来与
     * 从 {@code Collection<ConfigAttribute> getAttributes(String path)} 方法获取的
     * 权限集合进行比对
     * @param authentication 用户身份
     * @return 用户拥有的权限
     */
    Collection<ConfigAttribute> getAttributes(Authentication authentication);

    /**
     * 获取全部权限
     * @return 权限
     */
    Collection<ConfigAttribute> getAllConfigAttributes();
}
