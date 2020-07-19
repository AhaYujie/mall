package online.ahayujie.mall.admin.pms.bean.dto;

import lombok.Data;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;

import java.util.List;

/**
 * 包含 {@link online.ahayujie.mall.admin.pms.bean.model.ProductSpecification} 和
 * 对应的 {@link online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue}
 * 的商品规格DTO
 * @author aha
 * @since 2020/7/18
 */
@Data
public class ProductSpecificationDTO {
    private ProductSpecification specification;

    private ProductSpecificationValue specificationValue;

    public ProductSpecificationDTO() {
    }

    public ProductSpecificationDTO(ProductSpecification specification, ProductSpecificationValue specificationValue) {
        this.specification = specification;
        this.specificationValue = specificationValue;
    }
}
