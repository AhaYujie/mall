package online.ahayujie.mall.admin.oms.mapper;

import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单仅退款申请商品 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderRefundApplyProductMapper extends BaseMapper<OrderRefundApplyProduct> {

}
