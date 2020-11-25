package online.ahayujie.mall.portal.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryTreeDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;
import online.ahayujie.mall.portal.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.portal.pms.service.ProductCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryServiceImpl(ProductCategoryMapper productCategoryMapper) {
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public CommonPage<ProductCategoryDTO> getNavProductCategory(Integer pageNum, Integer pageSize) {
        Page<ProductCategoryDTO> page = new Page<>(pageNum, pageSize);
        Page<ProductCategoryDTO> productCategoryDTOPage = productCategoryMapper.selectPageByIsNav(page,
                ProductCategory.NavStatus.SHOW.getValue());
        return new CommonPage<>(productCategoryDTOPage);
    }

    @Override
    public List<ProductCategoryTreeDTO> getTreeList(Long parentId) {
        List<ProductCategoryTreeDTO> trees = new ArrayList<>();
        List<ProductCategoryDTO> roots = productCategoryMapper.selectAllByParentId(parentId);
        for (ProductCategoryDTO root : roots) {
            ProductCategoryTreeDTO tree = new ProductCategoryTreeDTO();
            tree.setProductCategory(root);
            tree.setChildren(getTreeList(root.getId()));
            trees.add(tree);
        }
        return trees;
    }
}
