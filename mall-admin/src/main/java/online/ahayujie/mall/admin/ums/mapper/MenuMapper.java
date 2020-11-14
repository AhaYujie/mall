package online.ahayujie.mall.admin.ums.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.ums.bean.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据上级菜单id分页查询
     * @param page 上级菜单id
     * @param parentId 分页
     * @return 菜单
     */
    IPage<Menu> selectByParentId(@Param("page") Page<?> page, @Param("parentId") Long parentId);

    /**
     * 查询全部菜单
     * @return 全部菜单
     */
    List<Menu> selectAll();

    /**
     * 根据上级菜单id查询全部
     * @param parentId 上级菜单id
     * @return 全部子菜单
     */
    List<Menu> selectAllByParentId(Long parentId);
}
