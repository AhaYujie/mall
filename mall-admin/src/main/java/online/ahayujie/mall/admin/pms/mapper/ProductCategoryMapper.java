package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品分类 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-10
 */
@Mapper
@Repository
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    /**
     * 查询全部
     * @return 全部商品分类
     */
    List<ProductCategory> selectAll();

    /**
     * 根据上级分类分页查询，根据sort从大到小排序
     * @param page 分页参数
     * @param parentId 上级分类id
     * @return 商品分类
     */
    IPage<ProductCategory> selectByParentId(@Param("page") Page<?> page, @Param("parentId") Long parentId);

    /**
     * 根据上级分类查询全部，根据sort从大到小排序
     * @param parentId 上级分类id
     * @return 商品分类
     */
    List<ProductCategory> selectAllByParentId(Long parentId);

    /**
     * 根据上级分类查询全部id
     * @param parentId 上级分类id
     * @return 全部分类id
     */
    List<Long> selectIdsByParentId(Long parentId);
}
