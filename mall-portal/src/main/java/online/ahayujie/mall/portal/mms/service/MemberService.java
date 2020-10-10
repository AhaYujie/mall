package online.ahayujie.mall.portal.mms.service;

import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginDTO;
import online.ahayujie.mall.portal.mms.bean.dto.MemberLoginParam;
import online.ahayujie.mall.portal.mms.bean.dto.MemberRegisterParam;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
public interface MemberService extends JwtUserDetailService {
    /**
     * 会员注册
     * @param param 用户注册参数
     * @throws DuplicateUsernameException 用户名已存在
     * @throws DuplicatePhoneException 手机号已存在
     */
    void register(MemberRegisterParam param) throws DuplicateUsernameException, DuplicatePhoneException;

    /**
     * 会员登录
     * @param param 用户登录参数
     * @return token
     * @throws UsernameNotFoundException 用户不存在
     * @throws BadCredentialsException 密码错误
     * @throws DisabledException 用户被禁用
     */
    MemberLoginDTO login(MemberLoginParam param) throws UsernameNotFoundException, BadCredentialsException, DisabledException;

    /**
     * 刷新accessToken，refreshToken不变。
     * @param refreshToken refreshToken
     * @return token
     * @throws IllegalArgumentException refreshToken不合法
     */
    MemberLoginDTO refreshAccessToken(String refreshToken) throws IllegalArgumentException;
}
