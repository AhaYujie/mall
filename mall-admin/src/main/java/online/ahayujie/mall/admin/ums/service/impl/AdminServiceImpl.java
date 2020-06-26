package online.ahayujie.mall.admin.ums.service.impl;

import io.jsonwebtoken.Claims;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.AdminRoleRelation;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.exception.admin.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.admin.IllegalRoleException;
import online.ahayujie.mall.admin.ums.mapper.AdminMapper;
import online.ahayujie.mall.admin.ums.mapper.AdminRoleRelationMapper;
import online.ahayujie.mall.admin.ums.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.common.exception.ApiException;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminRoleRelationMapper adminRoleRelationMapper;

    private RoleService roleService;
    private TokenProvider tokenProvider;
    private ResourceService resourceService;

    public AdminServiceImpl(AdminMapper adminMapper, PasswordEncoder passwordEncoder,
                            AdminRoleRelationMapper adminRoleRelationMapper) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.adminRoleRelationMapper = adminRoleRelationMapper;
    }

    @Override
    public Admin register(AdminRegisterParam param) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(param, admin);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setStatus(Admin.ACTIVE_STATUS);
        admin.setCreateTime(new Date());
        try {
            adminMapper.insert(admin);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUsernameException(e);
        }
        return admin;
    }

    @Override
    public AdminLoginDTO login(AdminLoginParam param) throws UsernameNotFoundException, BadCredentialsException {
        AdminUserDetailsDTO userDetails = (AdminUserDetailsDTO) loadUserByUsername(param.getUsername());
        log.debug(userDetails.toString());
        if (!passwordEncoder.matches(param.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        Map<String, Object> claims = getAdminClaims(userDetails);
        String accessToken = tokenProvider.createAccessToken(userDetails.getUsername(), claims);
        String refreshToken = tokenProvider.createRefreshToken(userDetails.getUsername(), claims);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AdminLoginDTO(accessToken, refreshToken, tokenProvider.getAccessTokenValidityInSeconds());
    }

    @Override
    public AdminLoginDTO refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("refreshToken不合法");
        }
        Claims refreshTokenClaims = tokenProvider.getClaimsFromRefreshToken(refreshToken);
        String username = refreshTokenClaims.getSubject();
        AdminUserDetailsDTO userDetails = (AdminUserDetailsDTO) loadUserByUsername(username);
        Map<String, Object> claims = getAdminClaims(userDetails);
        String accessToken = tokenProvider.createAccessToken(userDetails.getUsername(), claims);
        return new AdminLoginDTO(accessToken, refreshToken, tokenProvider.getAccessTokenValidityInSeconds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Long adminId, List<Long> roleIdList)
            throws UsernameNotFoundException, IllegalRoleException {
        if (roleIdList == null) {
            return;
        }
        // 检查用户是否存在
        if (adminMapper.selectById(adminId) == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 检查角色是否合法
        roleService.validateRole(roleIdList);
        // 删除用户原本的全部角色
        adminRoleRelationMapper.deleteByAdminId(adminId);
        // 添加新角色
        List<AdminRoleRelation> adminRoleRelations = roleIdList.stream()
                .map(roleId -> new AdminRoleRelation(null, null, new Date(), adminId, roleId))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(adminRoleRelations)) {
            adminRoleRelationMapper.insert(adminRoleRelations);
        }
    }

    @Override
    public AdminInfoDTO getAdminInfo() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities(Claims claims) {
        String authoritiesString = claims.get("auth", String.class);
        return AdminUserDetailsDTO.getAuthorities(authoritiesString);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<Resource> resourceList = resourceService.getResourceListByAdminId(admin.getId());
        return new AdminUserDetailsDTO(admin, resourceList);
    }

    private Map<String, Object> getAdminClaims(AdminUserDetailsDTO userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("id", userDetails.getAdmin().getId());
        claims.put("username", userDetails.getUsername());
        claims.put("auth", AdminUserDetailsDTO.getAuthoritiesString(userDetails.getAuthorities()));
        return claims;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Autowired
    public void setResourceService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }
}
