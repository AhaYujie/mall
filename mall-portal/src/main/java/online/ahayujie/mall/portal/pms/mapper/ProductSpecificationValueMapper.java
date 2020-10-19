package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.pms.bean.model.ProductSpecificationValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品规格选项 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface ProductSpecificationValueMapper extends BaseMapper<ProductSpecificationValue> {

}
