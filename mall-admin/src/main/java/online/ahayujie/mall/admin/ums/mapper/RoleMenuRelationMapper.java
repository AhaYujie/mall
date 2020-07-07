package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.RoleMenuRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-19
 */
@Mapper
@Repository
public interface RoleMenuRelationMapper extends BaseMapper<RoleMenuRelation> {
    /**
     * 根据角色id删除
     * @param roleId 角色id
     * @return 删除数量
     */
    int deleteByRoleId(Long roleId);

    /**
     * 批量插入
     * @param relations 角色菜单关系
     * @return 插入数量
     */
    int insert(@Param("list") List<RoleMenuRelation> relations);

    /**
     * 根据菜单id删除
     * @param menuId 菜单id
     * @return 删除数量
     */
    int deleteByMenuId(Long menuId);

    /**
     * 根据角色id查询
     * @param roleId 角色id
     * @return 角色菜单关系
     */
    List<RoleMenuRelation> selectByRoleId(Long roleId);
}
