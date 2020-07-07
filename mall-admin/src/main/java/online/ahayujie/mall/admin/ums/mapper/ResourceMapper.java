package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 获取全部资源
     * @return 资源
     */
    List<Resource> selectAll();

    /**
     * 根据资源分类id删除资源的分类，即设置category_id为0
     * @param categoryId 资源分类id
     * @return 更新数量
     */
    int deleteCategoryByCategoryId(Long categoryId);

    /**
     * 根据分类id查询
     * @param categoryId 分类id
     * @return 资源
     */
    List<Resource> selectByCategoryId(Long categoryId);
}
