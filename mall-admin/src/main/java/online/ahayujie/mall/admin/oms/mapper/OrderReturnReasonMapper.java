package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.model.OrderReturnReason;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 退货原因表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderReturnReasonMapper extends BaseMapper<OrderReturnReason> {
    /**
     * 分页查询。
     * 根据排序字段从大到小排序。
     * @param page 分页参数
     * @return 订单退款退货原因
     */
    IPage<OrderReturnReason> selectByPage(Page<OrderReturnReason> page);
}
