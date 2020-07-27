package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品sku Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface SkuMapper extends BaseMapper<Sku> {
    /**
     * 根据商品id查询
     * @param productId 商品id
     * @return sku
     */
    List<Sku> selectByProductId(Long productId);

    /**
     * 根据商品id查询
     * @param productId 商品id
     * @return sku
     */
    List<ProductDTO.SkuDTO> selectDTOByProductId(Long productId);

    /**
     * 根据商品id和sku编码模糊查询
     * @param productId 商品id
     * @param skuCode sku编码
     * @return sku
     */
    List<Sku> queryByProductIdAndSkuCode(@Param("productId") Long productId, @Param("skuCode") String skuCode);
}
