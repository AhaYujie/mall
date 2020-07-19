package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.SkuImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * sku图片 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface SkuImageMapper extends BaseMapper<SkuImage> {
    /**
     * 批量插入
     * @param skuImages sku图片
     * @return 插入数量
     */
    int insertList(@Param("list") List<SkuImage> skuImages);

    /**
     * 查询全部
     * @return 全部sku图片
     */
    List<SkuImage> selectAll();

    /**
     * 根据sku主键查询
     * @param skuId sku主键
     * @return sku图片
     */
    List<SkuImage> selectBySkuId(Long skuId);
}
