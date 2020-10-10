package online.ahayujie.mall.portal.mms.mapper;

import online.ahayujie.mall.portal.mms.bean.model.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-09
 */
@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {
    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 会员
     */
    Member selectByUsername(String username);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return 会员
     */
    Member selectByPhone(String phone);
}
