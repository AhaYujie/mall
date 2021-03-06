package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Sku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 商品sku Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface SkuMapper extends BaseMapper<Sku> {
    /**
     * 查询商品详情的sku
     * @param productId 商品id
     * @return sku
     */
    List<ProductDetailDTO.Sku> selectDetailSku(Long productId);

    /**
     * 查询商品id
     * @param id sku主键
     * @return 商品id
     */
    Long selectProductId(Long id);

    /**
     * 根据id查询price
     * @param ids 主键
     * @return price
     */
    List<Sku> selectPrice(List<Long> ids);

    /**
     * 更新库存。
     * 如果更新后的库存小于0则不更新。
     *
     * @param id sku主键
     * @param difference 差值，更新后的stock=更新前的stock+difference
     * @return 更新数量
     */
    Integer updateStock(@Param("id") Long id, @Param("difference") Integer difference);
}
