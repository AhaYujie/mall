package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.pms.bean.model.ProductImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品图片 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface ProductImageMapper extends BaseMapper<ProductImage> {
    /**
     * 查询商品详情的图片
     * @param productId 商品id
     * @return 图片
     */
    List<String> selectDetailImages(Long productId);
}
