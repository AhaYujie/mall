package online.ahayujie.mall.admin.ums.service;

import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginDTO;
import online.ahayujie.mall.admin.ums.bean.dto.AdminLoginParam;
import online.ahayujie.mall.admin.ums.bean.dto.AdminRegisterParam;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import online.ahayujie.mall.admin.ums.exception.admin.DuplicateUsernameException;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
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
     * 用户登录
     * @param param 用户登录参数
     * @return 登录结果
     * @throws UsernameNotFoundException 用户不存在
     * @throws BadCredentialsException 密码错误
     */
    AdminLoginDTO login(AdminLoginParam param) throws UsernameNotFoundException, BadCredentialsException;

    void updateRole(Long adminId, List<Long> roleIdList);
}
