package online.ahayujie.mall.portal.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;
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
 * @since 2020-10-13
 */
@Mapper
@Repository
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    /**
     * 根据上级分类获取所有。
     * 根据sort从大到小排序。
     *
     * @param parentId 上级分类id
     * @return 商品分类
     */
    List<ProductCategoryDTO> selectAllByParentId(@Param("parentId") Long parentId);

    /**
     * 根据isNav分页获取。
     * 根据sort从大到小排序。
     *
     * @param page 分页参数
     * @param isNav 是否显示在导航栏
     * @return 商品分类
     */
    Page<ProductCategoryDTO> selectPageByIsNav(@Param("page") Page<ProductCategoryDTO> page, @Param("isNav") Integer isNav);
}
