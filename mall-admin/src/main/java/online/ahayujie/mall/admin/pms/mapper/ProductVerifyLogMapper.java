package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.ProductVerifyLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品审核记录 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-04
 */
@Mapper
@Repository
public interface ProductVerifyLogMapper extends BaseMapper<ProductVerifyLog> {
    /**
     * 查询全部
     * @return 全部审核商品记录
     */
    List<ProductVerifyLog> selectAll();
}
