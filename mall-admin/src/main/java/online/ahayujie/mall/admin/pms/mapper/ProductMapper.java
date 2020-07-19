package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {
    /**
     * 查询全部
     * @return 全部商品
     */
    List<Product> selectAll();
}
