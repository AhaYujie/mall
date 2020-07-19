package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import online.ahayujie.mall.admin.pms.exception.IllegalProductSpecificationException;

import java.util.List;

/**
 * <p>
 * 商品规格 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
public interface ProductSpecificationService {
    /**
     * 检查商品规格选项合法性
     * @param specificationValue 商品规格选项
     * @throws IllegalProductSpecificationException 商品规格选项不合法
     */
    void validate(ProductSpecificationValue specificationValue) throws IllegalProductSpecificationException;

    /**
     * 保存商品规格信息
     * @param productSpecifications 商品规格信息
     * @return 保存成功后的商品规格信息
     */
    List<ProductSpecification> saveSpecifications(List<ProductSpecification> productSpecifications);

    /**
     * 保存商品规格选项信息
     * @param specificationValues 商品规格选项信息
     * @return 保存成功后的商品规格选项信息
     */
    List<ProductSpecificationValue> saveSpecificationValues(List<ProductSpecificationValue> specificationValues);
}
