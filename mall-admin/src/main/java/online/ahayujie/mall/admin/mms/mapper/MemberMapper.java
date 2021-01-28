package online.ahayujie.mall.admin.mms.mapper;

import online.ahayujie.mall.admin.mms.bean.model.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    /**
     * 分页查询
     * @param start 索引开始
     * @param size 大小
     * @return 会员
     */
    List<Member> selectByPage(@Param("start") Long start, @Param("size") Long size);

    /**
     * 根据用户名右模糊查询
     * @param start 索引开始
     * @param size 大小
     * @param username 用户名
     * @return 会员
     */
    List<Member> queryByUsername(@Param("start") Long start, @Param("size") Long size,
                                 @Param("username") String username);

    /**
     * 根据手机号右模糊查询
     * @param start 索引开始
     * @param size 大小
     * @param phone 手机号
     * @return 会员
     */
    List<Member> queryByPhone(@Param("start") Long start, @Param("size") Long size,
                              @Param("phone") String phone);

    /**
     * 根据用户名右模糊查询数量
     * @param username 用户名
     * @return 用户数量
     */
    Long selectCountByUsername(String username);

    /**
     * 根据id更新会员积分。
     * 更新后积分不能小于0，否则不更新。
     *
     * @param id 会员id
     * @param diff 积分差值：大于0是增加，小于0是减小
     * @return 更新数量
     */
    int updateIntegrationById(@Param("id") Long id, @Param("diff") Integer diff);

    /**
     * 根据手机号右模糊查询数量
     * @param phone 手机号
     * @return 用户数量
     */
    Long selectCountByPhone(String phone);
}
