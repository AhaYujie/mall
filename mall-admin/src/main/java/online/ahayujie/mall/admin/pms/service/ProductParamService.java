package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import online.ahayujie.mall.admin.pms.exception.IllegalProductParamException;

import java.util.List;

/**
 * <p>
 * 商品参数 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
public interface ProductParamService {
    /**
     * 检查商品参数合法性。
     * 若某一字段为null，则忽略该字段不检查
     * @param productParam 产品参数
     * @throws IllegalProductParamException 产品参数不合法
     */
    void validate(ProductParam productParam) throws IllegalProductParamException;

    /**
     * 保存商品参数
     * @param productParams 商品参数
     * @return 保存后的商品参数
     */
    List<ProductParam> save(List<ProductParam> productParams);

    /**
     * 根据商品id获取该商品的参数信息。
     * 若该商品不存在或者该商品没有参数信息则返回null。
     * @param productId 商品id
     * @return 商品参数
     */
    List<ProductParam> getByProductId(Long productId);
}
