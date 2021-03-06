package online.ahayujie.mall.admin.oms.mapper;

import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApplyProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单退货退款申请的商品 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderReturnApplyProductMapper extends BaseMapper<OrderReturnApplyProduct> {
    /**
     * 根据订单退货退款申请id查询
     * @param orderReturnApplyId 订单退货退款申请id
     * @return 订单退货退款申请商品
     */
    List<OrderReturnApplyProduct> selectByApplyId(Long orderReturnApplyId);
}
