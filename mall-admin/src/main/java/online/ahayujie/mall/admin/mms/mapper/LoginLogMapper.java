package online.ahayujie.mall.admin.mms.mapper;

import online.ahayujie.mall.admin.mms.bean.model.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

}
