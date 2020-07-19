package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.ProductSpecificationValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品规格选项 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Mapper
@Repository
public interface ProductSpecificationValueMapper extends BaseMapper<ProductSpecificationValue> {
    /**
     * 批量插入
     * @param specificationValues 商品规格选项
     * @return 插入数量
     */
    int insertList(@Param(("list")) List<ProductSpecificationValue> specificationValues);

    /**
     * 查询全部
     * @return 全部商品规格选项
     */
    List<ProductSpecificationValue> selectAll();

    /**
     * 根据商品规格id查询
     * @param specificationId 商品规格id
     * @return 商品规格选项
     */
    List<ProductSpecificationValue> selectBySpecificationId(Long specificationId);
}
