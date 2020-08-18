package online.ahayujie.mall.admin.oms.mapper;

import online.ahayujie.mall.admin.oms.bean.model.OrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单中的商品 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    /**
     * 根据订单id查询
     * @param orderId 订单id
     * @return 订单的商品
     */
    List<OrderProduct> selectByOrderId(Long orderId);
}
