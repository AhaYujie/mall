package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
}
