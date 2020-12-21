package online.ahayujie.mall.portal.oms.mapper;

import online.ahayujie.mall.portal.oms.bean.model.OrderProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 订单中的商品 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-11-07
 */
@Mapper
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {
    /**
     * 批量插入
     * @param orderProducts 订单商品
     * @return 插入数量
     */
    Integer insertList(@Param("list")List<OrderProduct> orderProducts);
}
