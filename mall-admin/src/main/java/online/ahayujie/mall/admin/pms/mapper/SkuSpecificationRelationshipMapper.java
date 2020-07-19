package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.SkuSpecificationRelationship;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * sku和商品规格关系 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface SkuSpecificationRelationshipMapper extends BaseMapper<SkuSpecificationRelationship> {
    /**
     * 批量插入
     * @param skuSpecificationRelationships sku和商品规格关系
     * @return 插入数量
     */
    int insertList(@Param("list") List<SkuSpecificationRelationship> skuSpecificationRelationships);

    /**
     * 查询全部
     * @return 全部sku与商品规格关系
     */
    List<SkuSpecificationRelationship> selectAll();

    /**
     * 根据sku主键查询
     * @param skuId sku主键
     * @return sku和商品规格关系
     */
    List<SkuSpecificationRelationship> selectBySkuId(Long skuId);
}
