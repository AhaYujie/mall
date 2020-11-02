package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import online.ahayujie.mall.admin.bean.model.MysqlExplain;
import online.ahayujie.mall.admin.pms.bean.dto.QueryProductParam;
import online.ahayujie.mall.admin.pms.bean.model.Product;
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
     * @param start 页开始
     * @param size 页大小
     * @return 商品列表
     */
    List<Product> selectByPage(@Param("start") Long start, @Param("size") Long size);

    /**
     * explain select * from pms_product
     * @return MysqlExplain
     */
    MysqlExplain explain();

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
     * 根据sort从大到小和创建时间从新到旧排序。
     * @param start 页索引
     * @param size 页大小
     * @param param 查询参数
     * @return 商品
     */
    List<Product> query(@Param("start") Long start, @Param("size") Long size, @Param("param") QueryProductParam param);

    /**
     * explain 查询商品
     * @param param 查询商品条件
     * @return MysqlExplain
     */
    MysqlExplain explainQuery(QueryProductParam param);

    /**
     * 根据商品分类id查询商品id
     * @param productCategoryId 商品分类id
     * @return 商品id
     */
    List<Long> selectIdsByProductCategoryId(Long productCategoryId);

    /**
     * 根据商品品牌id查询商品id
     * @param brandId 商品品牌id
     * @return 商品id
     */
    List<Long> selectIdsByBrandId(Long brandId);
}
