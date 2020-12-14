package online.ahayujie.mall.portal.oms.mapper;

import online.ahayujie.mall.portal.oms.bean.model.OrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单中的商品 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@Mapper
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

}
