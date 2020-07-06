package online.ahayujie.mall.admin.ums.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据名称关键词分页查询
     * @param page 分页
     * @param keyword 名称关键词
     * @return 角色
     */
    IPage<Role> queryByName(@Param("page") Page<?> page, @Param("keyword") String keyword);

    /**
     * 根据角色 id 查询角色相关菜单
     * @param roleId 角色 id
     * @return 菜单
     */
    List<Menu> selectMenusByRoleId(Long roleId);

    /**
     * 根据角色 id 查询角色相关资源
     * @param roleId 角色 id
     * @return 资源
     */
    List<Resource> selectResourceByRoleId(Long roleId);

    /**
     * 查询全部角色
     * @return 全部角色
     */
    List<Role> selectAll();
}
