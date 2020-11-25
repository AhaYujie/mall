package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryTreeDTO;
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
     * 分页获取导航栏商品分类.
     * 即商品分类的isNav={@link ProductCategory.NavStatus#SHOW}.
     * 按照sort从大到小排序.
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 商品分类
     */
    CommonPage<ProductCategoryDTO> getNavProductCategory(Integer pageNum, Integer pageSize);

    /**
     * 根据上级分类递归获取树形结构的商品分类.
     * 例如上级分类A有下一级分类a,b,c;
     * a分类有下一级分类i,ii,iii;
     * 则返回包括a,b,c,i,ii,iii.
     * 按照sort从大到小排序.
     *
     * @param parentId 上级分类id
     * @return 树形结构的商品分类
     */
    List<ProductCategoryTreeDTO> getTreeList(Long parentId);
}
