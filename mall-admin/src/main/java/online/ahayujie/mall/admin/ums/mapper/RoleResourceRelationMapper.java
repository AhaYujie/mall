package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.RoleResourceRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台角色资源关系表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-08
 */
@Mapper
@Repository
public interface RoleResourceRelationMapper extends BaseMapper<RoleResourceRelation> {
    /**
     * 根据角色id查询
     * @param roleId 角色id
     * @return 角色资源关系
     */
    List<RoleResourceRelation> selectByRoleId(Long roleId);

    /**
     * 根据角色id删除
     * @param roleId 角色id
     * @return 删除数量
     */
    int deleteByRoleId(Long roleId);

    /**
     * 批量插入角色资源关系
     * @param relations 角色资源关系
     * @return 插入数量
     */
    int insert(@Param("list") List<RoleResourceRelation> relations);
}
