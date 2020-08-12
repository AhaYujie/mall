package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.model.OrderRefundReason;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单仅退款原因 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderRefundReasonMapper extends BaseMapper<OrderRefundReason> {
    /**
     * 分页获取。
     * 根据排序字段从大到小排序。
     * @param page 分页参数
     * @return 订单仅退款原因
     */
    IPage<OrderRefundReason> selectByPage(Page<OrderRefundReason> page);
}
