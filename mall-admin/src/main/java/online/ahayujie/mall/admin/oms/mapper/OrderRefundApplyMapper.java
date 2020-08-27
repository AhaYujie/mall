package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单仅退款申请 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderRefundApplyMapper extends BaseMapper<OrderRefundApply> {
    /**
     * 分页查询。
     * 根据创建时间从新到旧排序。
     *
     * @param page 分页参数
     * @return 订单仅退款申请
     */
    IPage<OrderRefundApply> selectByPage(@Param("page") Page<OrderRefundApply> page);

    /**
     * 分页查询。
     * 查询条件之间的关系为且。
     * 根据创建时间从新到旧排序。
     *
     * @param page 分页参数
     * @param orderSn 订单号
     * @param status 申请状态
     * @return 订单仅退款申请
     */
    IPage<OrderRefundApply> queryByPage(@Param("page") Page<OrderRefundApply> page, @Param("orderSn") String orderSn,
                                        @Param("status") Integer status);

    /**
     * 根据订单id查询
     * @param orderId 订单id
     * @return 订单仅退款申请
     */
    List<OrderRefundApply> queryByOrderId(Long orderId);
}
