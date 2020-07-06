package online.ahayujie.mall.admin.ums.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.event.DeleteAdminEvent;
import online.ahayujie.mall.admin.ums.exception.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.IllegalAdminStatusException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.admin.ums.mapper.AdminMapper;
import online.ahayujie.mall.admin.ums.mapper.AdminRoleRelationMapper;
import online.ahayujie.mall.admin.ums.service.AdminService;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.admin.ums.service.RoleService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.security.jwt.TokenProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final AdminRoleRelationMapper adminRoleRelationMapper;

    private RoleService roleService;
    private TokenProvider tokenProvider;
    private ResourceService resourceService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.header-prefix}")
    private String jwtHeaderPrefix;

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
        // TODO:用户登录日志收集
        AdminUserDetailsDTO userDetails = (AdminUserDetailsDTO) loadUserByUsername(param.getUsername());
        log.debug(userDetails.toString());
        if (!passwordEncoder.matches(param.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        if (!userDetails.isEnabled()) {
            throw new DisabledException("用户被禁用");
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
        // 检查用户是否存在
        if (adminMapper.selectById(adminId) == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        roleService.updateAdminRole(adminId, roleIdList);
    }

    @Override
    public AdminInfoDTO getAdminInfo() {
        Admin admin = getAdminFromToken();
        if (admin == null) {
            return null;
        }
        Long id = admin.getId();
        admin = adminMapper.selectById(id);
        List<Role> roles = roleService.getRoleListByAdminId(id);
        AdminInfoDTO adminInfoDTO = new AdminInfoDTO();
        BeanUtils.copyProperties(admin, adminInfoDTO);
        adminInfoDTO.setRoles(roles);
        return adminInfoDTO;
    }

    @Override
    public Admin getAdminFromToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String bearerToken = request.getHeader(jwtHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtHeaderPrefix)) {
            return getAdminFromToken(bearerToken.substring(jwtHeaderPrefix.length()));
        }
        return null;
    }

    @Override
    public Admin getAdminFromToken(String token) {
        Claims claims;
        try {
            claims = tokenProvider.getClaimsFromAccessToken(token);
        }
        catch (Exception e) {
            try {
                claims = tokenProvider.getClaimsFromRefreshToken(token);
            }
            catch (Exception e1) {
                return null;
            }
        }
        Admin admin = new Admin();
        admin.setId(claims.get("id", Long.class));
        admin.setUsername(claims.get("username", String.class));
        return admin;
    }

    @Override
    public CommonPage<Admin> getAdminList(String keyword, Integer pageNum, Integer pageSize) {
        Page<Admin> page = new Page<>(pageNum, pageSize);
        IPage<Admin> admins = adminMapper.selectByUsernameAndNickName(page, keyword);
        return new CommonPage<>(admins);
    }

    @Override
    public void updateAdmin(Long id, UpdateAdminParam param) throws DuplicateUsernameException, IllegalAdminStatusException {
        Admin admin = new Admin();
        BeanUtils.copyProperties(param, admin);
        admin.setId(id);
        if (admin.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        if (admin.getStatus() != null) {
            Integer status = admin.getStatus();
            if (!Admin.ACTIVE_STATUS.equals(status) && !Admin.UN_ACTIVE_STATUS.equals(status)) {
                throw new IllegalAdminStatusException("用户状态不合法");
            }
        }
        admin.setUpdateTime(new Date());
        try {
            adminMapper.updateById(admin);
        } catch (DuplicateKeyException e) {
            throw new DuplicateUsernameException(e);
        }
    }

    @Override
    public void updatePassword(UpdateAdminPasswordParam param) throws UsernameNotFoundException, BadCredentialsException {
        Admin oldAdmin = adminMapper.selectByUsername(param.getUsername());
        if (oldAdmin == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (!passwordEncoder.matches(param.getOldPassword(), oldAdmin.getPassword())) {
            throw new BadCredentialsException("原密码错误");
        }
        UpdateAdminParam updateAdminParam = new UpdateAdminParam();
        updateAdminParam.setPassword(param.getNewPassword());
        updateAdmin(oldAdmin.getId(), updateAdminParam);
    }

    @Override
    public Admin getById(Long id) {
        return adminMapper.selectById(id);
    }

    @Override
    public int removeById(Long id) {
        Admin admin = adminMapper.selectById(id);
        int count = adminMapper.deleteById(id);
        if (count != 0) {
            applicationEventPublisher.publishEvent(new DeleteAdminEvent(admin));
        }
        return count;
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
        List<Role> roles = roleService.getRoleListByAdminId(admin.getId());
        List<Long> roleIds = roles.stream().map(Base::getId).collect(Collectors.toList());
        List<Resource> resourceList = resourceService.getResourceListByRoleIds(roleIds);
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

    @Override
    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
