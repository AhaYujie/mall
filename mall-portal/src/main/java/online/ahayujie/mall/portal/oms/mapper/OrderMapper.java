package online.ahayujie.mall.portal.oms.mapper;

import online.ahayujie.mall.portal.oms.bean.model.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@Mapper
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
