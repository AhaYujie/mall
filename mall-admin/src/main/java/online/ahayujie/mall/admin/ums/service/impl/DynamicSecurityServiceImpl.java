package online.ahayujie.mall.admin.ums.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.AdminUserDetailsDTO;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.security.component.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aha
 * @date 2020/6/7
 */
@Slf4j
@Service
public class DynamicSecurityServiceImpl implements DynamicSecurityService {
    private ResourceService resourceService;

    @Override
    public Collection<ConfigAttribute> getAttributes(String path) {
        List<Resource> resources = resourceService.list();
        List<ConfigAttribute> configAttributes = new ArrayList<>();
        PathMatcher pathMatcher = new AntPathMatcher();
        for (Resource resource : resources) {
            if (pathMatcher.match(resource.getUrl(), path)) {
                configAttributes.add(AdminUserDetailsDTO.getConfigAttribute(resource));
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Authentication authentication) {
        return AdminUserDetailsDTO.getConfigAttributes(authentication.getAuthorities());
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return resourceService.list().stream()
                .map(AdminUserDetailsDTO::getConfigAttribute)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
