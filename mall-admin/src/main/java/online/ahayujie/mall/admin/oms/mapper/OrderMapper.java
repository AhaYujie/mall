package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.OrderListDTO;
import online.ahayujie.mall.admin.oms.bean.dto.QueryOrderListParam;
import online.ahayujie.mall.admin.oms.bean.model.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 分页查询订单列表。
     * 查询参数中某一字段为null则忽略不作为查询条件。
     * 查询参数之间的关系为且。
     * 订单列表按照创建时间从新到旧排序。
     * @param page 分页参数
     * @param param 查询参数
     * @return 订单列表
     */
    IPage<OrderListDTO> queryList(@Param("page") Page<OrderListDTO> page, @Param("param") QueryOrderListParam param);

    /**
     * 根据订单id查询订单状态
     * @param id 订单id
     * @return 订单状态
     */
    Integer selectOrderStatus(Long id);
}
