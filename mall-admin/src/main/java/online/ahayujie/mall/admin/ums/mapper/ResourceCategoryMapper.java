package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 资源分类表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface ResourceCategoryMapper extends BaseMapper<ResourceCategory> {
    /**
     * 获取全部资源分类，根据 sort 从大到小排序
     * @return 资源分类
     */
    List<ResourceCategory> selectAll();

    /**
     * 根据id删除资源分类
     * @param id 资源分类id
     * @return 删除数量
     */
    int deleteById(Long id);
}
