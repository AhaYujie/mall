package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;

import java.util.List;

/**
 * @author aha
 * @date 2020/7/11
 */
@Data
public class ProductCategoryTree {
    private ProductCategory productCategory;

    private List<ProductCategoryTree> children;

    public ProductCategoryTree() {
    }

    public ProductCategoryTree(ProductCategory productCategory, List<ProductCategoryTree> children) {
        this.productCategory = productCategory;
        this.children = children;
    }
}
