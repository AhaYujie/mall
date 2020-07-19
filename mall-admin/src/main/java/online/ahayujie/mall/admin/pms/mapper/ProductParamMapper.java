package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.ProductParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品参数 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface ProductParamMapper extends BaseMapper<ProductParam> {
    /**
     * 批量插入
     * @param productParams 商品参数
     * @return 插入数量
     */
    int insertList(@Param("list") List<ProductParam> productParams);

    /**
     * 查询全部
     * @return 全部商品参数
     */
    List<ProductParam> selectAll();

    /**
     * 根据商品id查询
     * @param productId 商品id
     * @return 商品参数
     */
    List<ProductParam> selectByProductId(Long productId);
}
