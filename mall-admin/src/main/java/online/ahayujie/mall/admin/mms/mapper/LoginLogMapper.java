package online.ahayujie.mall.admin.mms.mapper;

import online.ahayujie.mall.admin.mms.bean.model.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 会员登录记录 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
@Mapper
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {
    /**
     * 分页获取会员的登录记录
     * @param start 索引开始
     * @param size 大小
     * @param memberId 会员id
     * @return 会员的登录记录
     */
    List<LoginLog> selectPageByMemberId(@Param("start") Long start, @Param("size") Long size,
                                        @Param("memberId") Long memberId);

    /**
     * 根据会员id查询数量
     * @param memberId 会员id
     * @return 登录记录数量
     */
    Long selectCountByMemberId(Long memberId);
}
