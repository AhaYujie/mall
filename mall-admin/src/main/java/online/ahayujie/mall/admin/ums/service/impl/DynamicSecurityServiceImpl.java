package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.security.component.DynamicSecurityService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author aha
 * @date 2020/6/7
 */
public class DynamicSecurityServiceImpl implements DynamicSecurityService {
    @Override
    public Collection<ConfigAttribute> getAttributes(String path) {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Authentication authentication) {
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
}
