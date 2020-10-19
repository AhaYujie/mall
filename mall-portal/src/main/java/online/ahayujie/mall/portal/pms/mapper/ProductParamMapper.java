package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品参数 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface ProductParamMapper extends BaseMapper<ProductParam> {
    /**
     * 查询商品详情的商品参数
     * @param productId 商品id
     * @return 商品参数
     */
    List<ProductDetailDTO.Param> selectDetailParam(Long productId);
}
