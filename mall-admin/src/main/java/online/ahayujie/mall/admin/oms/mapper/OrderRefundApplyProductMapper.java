package online.ahayujie.mall.admin.oms.mapper;

import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApplyProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    /**
     * 根据订单仅退款申请id查询
     * @param orderRefundApplyId 订单仅退款申请id
     * @return 订单仅退款申请商品
     */
    List<OrderRefundApplyProduct> selectByApplyId(Long orderRefundApplyId);
}
