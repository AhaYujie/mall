package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 后台用户权限表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

}
