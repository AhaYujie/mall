package online.ahayujie.mall.portal.mms.service;

import online.ahayujie.mall.portal.mms.bean.dto.*;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.exception.DuplicatePhoneException;
import online.ahayujie.mall.portal.mms.exception.DuplicateUsernameException;
import online.ahayujie.mall.security.jwt.JwtUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 从http request header中的accessToken获取会员信息。
     * 如果accessToken不存在则返回null。
     *
     * @return 会员，包含id和用户名。
     */
    Member getMemberFromToken();

    /**
     * 根据accessToken或者refreshToken获取会员信息。
     * 如果token不合法则返回null。
     *
     * @param token accessToken或者refreshToken
     * @return 会员，包含id和用户名。
     */
    Member getMemberFromToken(String token);

    /**
     * 获取会员信息
     * @return 会员信息
     */
    MemberDTO getInfo();

    /**
     * 更新会员信息
     * @param param 会员信息参数
     * @throws IllegalArgumentException 参数不合法
     */
    void updateInfo(UpdateMemberParam param) throws IllegalArgumentException;

    /**
     * 更新会员的积分。
     * 此接口为事务方法。如果调用此接口的上层事务方法抛出 {@link Exception} 异常，
     * 则回滚此接口的所有操作。
     *
     * @param id 会员id
     * @param integration 会员的积分
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    void updateIntegration(Long id, Integer integration);
}
