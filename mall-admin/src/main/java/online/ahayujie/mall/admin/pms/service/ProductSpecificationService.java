package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
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
     * 保存商品规格信息。
     * 若 {@code productSpecifications} 为null或空则返回null。
     * @param productSpecifications 商品规格信息
     * @return 保存成功后的商品规格信息
     */
    List<ProductSpecification> saveSpecifications(List<ProductSpecification> productSpecifications);

    /**
     * 保存商品规格选项信息，如果 {@code specificationValues} 为空则返回null。
     * @param specificationValues 商品规格选项信息
     * @return 保存成功后的商品规格选项信息
     */
    List<ProductSpecificationValue> saveSpecificationValues(List<ProductSpecificationValue> specificationValues);

    /**
     * 根据商品id获取商品的规格信息。
     * 若该商品不存在或该商品无规格信息则返回null。
     * @param productId 商品id
     * @return 商品规格信息
     */
    List<ProductDTO.SpecificationDTO> getByProductId(Long productId);

    /**
     * 根据商品规格id获取商品规格，若不存在则返回null。
     * @param id 商品规格id
     * @return 商品规格
     */
    ProductSpecification getSpecificationById(Long id);

    /**
     * 根据商品规格选项id获取商品规格选项，若不存在则返回null。
     * @param id 商品规格选项id
     * @return 商品规格选项
     */
    ProductSpecificationValue getSpecificationValueById(Long id);
}
