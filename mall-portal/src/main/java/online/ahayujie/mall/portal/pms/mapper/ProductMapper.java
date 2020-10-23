package online.ahayujie.mall.portal.pms.mapper;

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
     * @param isVerify 是否审核通过
     * @return 商品详情
     */
    ProductDetailDTO.ProductInfo selectDetail(@Param("id") Long id, @Param("isPublish") Integer isPublish,
                                              @Param("isVerify") Integer isVerify);

    /**
     * 查询移动端商品详情
     * @param id 商品id
     * @param isPublish 是否上架
     * @param isVerify 是否审核通过
     * @return 移动端商品详情
     */
    ProductDetailDTO.ProductInfo selectMobileDetail(@Param("id") Long id, @Param("isPublish") Integer isPublish,
                                                    @Param("isVerify") Integer isVerify);

    /**
     * 查询是否上架和是否审核通过
     * @param id 商品id
     * @return 是否上架和是否审核通过
     */
    Product selectIsPublishAndIsVerify(Long id);

    /**
     * 查询是否上架
     * @param ids 商品id
     * @return 商品id和isPublish
     */
    List<Product> selectIsPublish(@Param("list") List<Long> ids);
}
