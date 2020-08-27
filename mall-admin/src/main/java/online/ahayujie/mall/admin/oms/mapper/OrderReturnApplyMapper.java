package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    /**
     * 分页查询
     * 根据创建时间从新到旧排序。
     * @param page 分页参数
     * @return 订单退货退款申请
     */
    IPage<OrderReturnApply> selectByPage(@Param("page") Page<OrderReturnApply> page);

    /**
     * 分页查询。
     * 查询条件之间的关系为且。
     * 根据创建时间从新到旧排序。
     *
     * @param page 分页参数
     * @param orderSn 订单号
     * @param status 申请状态
     * @return 订单退货退款申请
     */
    IPage<OrderReturnApply> queryByPage(@Param("page") Page<OrderReturnApply> page, @Param("orderSn") String orderSn,
                                        @Param("status") Integer status);

    /**
     * 根据订单id查询
     * @param orderId 订单id
     * @return 订单退货退款申请
     */
    List<OrderReturnApply> selectByOrderId(Long orderId);
}
