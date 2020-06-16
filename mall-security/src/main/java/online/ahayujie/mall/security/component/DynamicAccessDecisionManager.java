package online.ahayujie.mall.security.component;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 动态权限决策管理器
 * 判断用户是否有访问权限
 * @author aha
 * @date 2020/6/7
 */
@Slf4j
@Component
public class DynamicAccessDecisionManager implements AccessDecisionManager {
    private DynamicSecurityService dynamicSecurityService;

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (CollectionUtils.isEmpty(configAttributes)) {
            return;
        }
        log.debug("configAttributes: " + configAttributes);
        Collection<ConfigAttribute> userConfigAttributes = dynamicSecurityService.getAttributes(authentication);
        log.debug("userConfigAttributes: " + userConfigAttributes);
        if (CollectionUtils.isEmpty(userConfigAttributes)) {
            throw new AccessDeniedException(ResultCodeEnum.FORBIDDEN.getMessage());
        }
        for (ConfigAttribute needConfigAttribute : configAttributes) {
            for (ConfigAttribute userConfigAttribute : userConfigAttributes) {
                if (needConfigAttribute.getAttribute().equals(userConfigAttribute.getAttribute())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException(ResultCodeEnum.FORBIDDEN.getMessage());
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Autowired(required = false)
    public void setDynamicSecurityService(DynamicSecurityService dynamicSecurityService) {
        this.dynamicSecurityService = dynamicSecurityService;
    }
}
