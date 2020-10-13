package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;

import java.util.List;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
public interface ProductCategoryService {
    /**
     * 获取所有一级分类。
     * 商品分类的isShow = {@link ProductCategory.ShowStatus#SHOW}，
     * 根据sort从大到小排序。
     *
     * @return 商品一级分类
     */
    List<ProductCategoryDTO> getFirstLevel();

    /**
     * 分页获取二级分类。
     * 商品分类的isShow = {@link ProductCategory.ShowStatus#SHOW}，
     * 根据sort从大到小排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param parentId 二级分类的上级分类id
     * @return 商品二级分类
     */
    CommonPage<ProductCategoryDTO> getSecondLevel(Long pageNum, Long pageSize, Long parentId);
}
