package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.dto.QueryProductParam;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 分页获取，根据排序字段和创建时间排序
     * @param page 分页参数
     * @return 商品列表
     */
    IPage<Product> selectPage(@Param("page") Page<?> page);

    /**
     * 根据商品id列表更新
     * @param ids 商品id列表
     * @param product 商品信息
     * @return 更新数量
     */
    int updateByIds(@Param("list") List<Long> ids, @Param("product") Product product);

    /**
     * 根据商品分类id更新
     * @param productCategoryId 商品分类id
     * @param product 商品信息
     * @return 更新数量
     */
    int updateByProductCategoryId(@Param("productCategoryId") Long productCategoryId, @Param("product") Product product);

    /**
     * 根据品牌id更新
     * @param brandId 品牌id
     * @param product 商品信息
     * @return 更新数量
     */
    int updateByBrandId(@Param("brandId") Long brandId, @Param("product") Product product);

    /**
     * 根据商品名称(模糊)，货号(模糊)，分类，品牌，上架状态，新品状态，推荐状态，审核状态，预告状态分页查询。
     * 若某一字段为null则不作为查询条件。
     * 条件之间的关系为且(and)。
     * 根据创建时间从新到旧排序。
     * @param page 分页参数
     * @param param 查询参数
     * @return 商品
     */
    IPage<Product> query(@Param("page") Page<?> page, @Param("param") QueryProductParam param);
}
