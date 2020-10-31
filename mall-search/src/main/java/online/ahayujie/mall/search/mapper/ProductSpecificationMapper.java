package online.ahayujie.mall.search.mapper;

import online.ahayujie.mall.search.bean.dto.ProductSpecificationDTO;
import online.ahayujie.mall.search.bean.model.ProductSpecification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品规格 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-27
 */
@Mapper
@Repository
public interface ProductSpecificationMapper extends BaseMapper<ProductSpecification> {
    /**
     * 根据productId查询
     * @param productId 商品id
     * @return 商品规格
     */
    List<ProductSpecificationDTO> selectDTOByProductId(Long productId);
}
