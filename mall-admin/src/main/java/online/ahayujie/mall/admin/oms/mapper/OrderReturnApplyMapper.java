package online.ahayujie.mall.admin.oms.mapper;

import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单退货退款申请 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderReturnApplyMapper extends BaseMapper<OrderReturnApply> {

}
