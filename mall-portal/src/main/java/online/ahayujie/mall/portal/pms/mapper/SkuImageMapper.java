package online.ahayujie.mall.portal.pms.mapper;

import online.ahayujie.mall.portal.pms.bean.model.SkuImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * sku图片 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@Mapper
@Repository
public interface SkuImageMapper extends BaseMapper<SkuImage> {
    /**
     * 根据sku主键查询
     * @param skuId sku主键
     * @return 图片
     */
    List<String> selectBySkuId(Long skuId);
}
