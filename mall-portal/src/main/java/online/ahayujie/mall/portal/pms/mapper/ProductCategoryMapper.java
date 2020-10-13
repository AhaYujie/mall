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
     * 获取所有一级分类。
     * 商品分类的isShow = {@code isShow}，
     * 根据sort从大到小排序。
     *
     * @param parentId 一级分类的父类id
     * @param isShow 是否显示
     * @return 商品一级分类
     */
    List<ProductCategoryDTO> selectFirstLevel(@Param("parentId") Long parentId, @Param("isShow") Integer isShow);

    /**
     * 分页查询二级分类。
     * 商品分类的isShow = {@code isShow}，
     * 根据sort从大到小排序。
     *
     * @param page 分页参数
     * @param parentId 上级分类id
     * @param isShow 是否显示
     * @return 商品二级分类
     */
    Page<ProductCategoryDTO> selectSecondLevel(@Param("page") Page<ProductCategoryDTO> page, @Param("parentId") Long parentId,
                                               @Param("isShow") Integer isShow);
}
