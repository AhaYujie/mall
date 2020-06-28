package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.*;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.DuplicateUsernameException;
import online.ahayujie.mall.admin.ums.exception.IllegalAdminStatusException;
import online.ahayujie.mall.admin.ums.exception.IllegalRoleException;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
public interface AdminService extends IService<Admin>, JwtUserDetailService {
    /**
     * 用户注册
     * @param param 用户注册参数
     * @return 用户
     * @throws DuplicateUsernameException 重复用户名
     */
    Admin register(AdminRegisterParam param) throws DuplicateUsernameException;

    /**
     * 刷新 accessToken
     * @param refreshToken 旧的 accessToken
     * @return 新的 accessToken
     * @throws IllegalArgumentException refreshToken 不合法
     */
    AdminLoginDTO refreshAccessToken(String refreshToken) throws IllegalArgumentException;

    /**
     * 用户登录
     * @param param 用户登录参数
     * @return 登录结果
     * @throws UsernameNotFoundException 用户不存在
     * @throws BadCredentialsException 密码错误
     * @throws DisabledException 用户被禁用
     */
    AdminLoginDTO login(AdminLoginParam param) throws UsernameNotFoundException, BadCredentialsException, DisabledException;

    /**
     * 更新用户的角色，
     * 如果 roleIdList 为 null，则不做任何处理，
     * 如果 roleIdList 为空，则删除用户的所有角色
     * @param adminId 用户id
     * @param roleIdList 角色id
     * @throws UsernameNotFoundException 用户不存在
     * @throws IllegalRoleException 角色id不合法
     */
    void updateRole(Long adminId, List<Long> roleIdList) throws UsernameNotFoundException, IllegalRoleException;

    /**
     * 获取当前登录的后台用户信息，
     * 若未登录则返回 null
     * @return 后台用户信息
     */
    AdminInfoDTO getAdminInfo();

    /**
     * 从request header中的accessToken获取后台用户信息，
     * 若accessToken不存在则返回null
     * @return 后台用户信息
     */
    Admin getAdminFromToken();

    /**
     * 从token中获取后台用户信息
     * @param token accessToken或者refreshToken
     * @return 后台用户信息
     */
    Admin getAdminFromToken(String token);

    /**
     * 根据用户名或昵称分页查询后台用户
     * @param keyword 关键词
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 后台用户
     */
    CommonPage<Admin> getAdminList(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 更新后台用户信息
     * @param id 后台用户id
     * @param param 后台用户信息
     * @throws DuplicateUsernameException 用户名重复
     * @throws IllegalAdminStatusException 用户状态不合法
     */
    void updateAdmin(Long id, UpdateAdminParam param) throws DuplicateUsernameException, IllegalAdminStatusException;

    /**
     * 更新后台用户密码
     * @param param 参数
     * @throws UsernameNotFoundException 用户不存在
     * @throws BadCredentialsException 原密码错误
     */
    void updatePassword(UpdateAdminPasswordParam param) throws UsernameNotFoundException, BadCredentialsException;
}
