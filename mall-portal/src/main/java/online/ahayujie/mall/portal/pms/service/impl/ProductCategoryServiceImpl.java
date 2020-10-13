package online.ahayujie.mall.portal.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;
import online.ahayujie.mall.portal.pms.mapper.ProductCategoryMapper;
import online.ahayujie.mall.portal.pms.service.ProductCategoryService;
import org.springframework.stereotype.Service;

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
    public List<ProductCategoryDTO> getFirstLevel() {
        return productCategoryMapper.selectFirstLevel(ProductCategory.NON_PARENT_ID, ProductCategory.ShowStatus.SHOW.getValue());
    }

    @Override
    public CommonPage<ProductCategoryDTO> getSecondLevel(Long pageNum, Long pageSize, Long parentId) {
        Page<ProductCategoryDTO> page = new Page<>(pageNum, pageSize);
        Page<ProductCategoryDTO> productCategoryDTOPage = productCategoryMapper.selectSecondLevel(page, parentId,
                ProductCategory.ShowStatus.SHOW.getValue());
        return new CommonPage<>(productCategoryDTOPage);
    }
}
