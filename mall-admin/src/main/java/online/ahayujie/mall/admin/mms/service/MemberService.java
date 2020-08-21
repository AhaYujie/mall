package online.ahayujie.mall.admin.mms.service;

import online.ahayujie.mall.admin.mms.bean.model.LoginLog;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
public interface MemberService {
    /**
     * 分页获取会员列表
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 会员列表
     */
    CommonPage<Member> list(Long pageNum, Long pageSize);

    /**
     * 根据用户名右模糊查询会员。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param username 用户名
     * @return 会员
     */
    CommonPage<Member> queryByUsername(Long pageNum, Long pageSize, String username);

    /**
     * 根据手机号右模糊查询会员。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param phone 手机号
     * @return 会员
     */
    CommonPage<Member> queryByPhone(Long pageNum, Long pageSize, String phone);

    /**
     * 分页获取会员的登录记录
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param id 会员id
     * @return 登录记录
     */
    CommonPage<LoginLog> getLoginLog(Long pageNum, Long pageSize, Long id);
}
