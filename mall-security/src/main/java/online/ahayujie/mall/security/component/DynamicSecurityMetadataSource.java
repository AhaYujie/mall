package online.ahayujie.mall.security.component;

import cn.hutool.core.util.URLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 动态权限数据源
 * @author aha
 * @date 2020/6/7
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private DynamicSecurityService dynamicSecurityService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        String url = ((FilterInvocation) o).getRequestUrl();
        String path = URLUtil.getPath(url);
        return dynamicSecurityService.getAttributes(path);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return dynamicSecurityService == null ? null : dynamicSecurityService.getAllConfigAttributes();
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
