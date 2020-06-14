package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 用户
     */
    Admin selectByUsername(String username);
}
