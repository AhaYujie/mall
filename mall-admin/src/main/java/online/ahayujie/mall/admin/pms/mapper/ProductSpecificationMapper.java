package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.ProductSpecification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品规格 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface ProductSpecificationMapper extends BaseMapper<ProductSpecification> {
    /**
     * 批量插入
     * @param productSpecifications 商品规格
     * @return 插入数量
     */
    int insertList(@Param("list") List<ProductSpecification> productSpecifications);

    /**
     * 查询全部
     * @return 全部商品规格
     */
    List<ProductSpecification> selectAll();

    /**
     * 根据商品id查询
     * @param productId 商品id
     * @return 商品规格
     */
    List<ProductSpecification> selectByProductId(Long productId);
}
