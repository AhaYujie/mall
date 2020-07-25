package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.ProductImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品图片 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface ProductImageMapper extends BaseMapper<ProductImage> {
    /**
     * 根据商品id查询
     * @param productId 商品id
     * @return 商品图片
     */
    List<ProductImage> selectByProductId(Long productId);

    /**
     * 批量插入
     * @param productImages 商品图片
     * @return 插入数量
     */
    int insertList(@Param("list") List<ProductImage> productImages);

    /**
     * 根据商品id删除
     * @param productId 商品id
     * @return 删除数量
     */
    int deleteByProductId(Long productId);
}
