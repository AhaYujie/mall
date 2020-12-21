package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.oms.bean.dto.ConfirmOrderDTO;
import online.ahayujie.mall.portal.oms.bean.dto.SubmitOrderProductDTO;
import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 查询商品详情
     * @param id 商品id
     * @param isPublish 是否上架
     * @return 商品详情
     */
    ProductDetailDTO.ProductInfo selectDetail(@Param("id") Long id, @Param("isPublish") Integer isPublish);

    /**
     * 查询移动端商品详情
     * @param id 商品id
     * @param isPublish 是否上架
     * @return 移动端商品详情
     */
    ProductDetailDTO.ProductInfo selectMobileDetail(@Param("id") Long id, @Param("isPublish") Integer isPublish);

    /**
     * 查询是否上架
     * @param ids 商品id
     * @return 商品id和isPublish
     */
    List<Product> selectIsPublishBatch(@Param("list") List<Long> ids);

    /**
     * 查询是否上架
     * @param id 商品id
     * @return 商品id和isPublish
     */
    Product selectIsPublish(Long id);

    /**
     * 根据id和isPublish查询
     * @param id 商品id
     * @param isPublish 是否上架
     * @return 商品
     */
    Product selectByIdAndIsPublish(@Param("id") Long id, @Param("isPublish") Integer isPublish);

    /**
     * 查询确认订单的商品信息
     * @param ids 商品id
     * @param isPublish 是否上架
     * @return 确认订单的商品信息
     */
    List<ConfirmOrderDTO.Product> selectConfirmOrderProductBatch(@Param("list") List<Long> ids,
                                                                 @Param("isPublish") Integer isPublish);

    /**
     * 查询提交订单的商品信息
     * @param ids 商品id
     * @param isPublish 是否上架
     * @return 提交订单的商品信息
     */
    List<SubmitOrderProductDTO> selectSubmitOrderProductBatch(@Param("list") List<Long> ids,
                                                              @Param("isPublish") Integer isPublish);
}
