package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-07-08
 */
@Mapper
@Repository
public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 查询全部
     * @return 全部品牌
     */
    List<Brand> selectAll();

    /**
     * 根据名称关键词分页查询，根据sort从大到小排序
     * @param page 分页参数
     * @param keyword 名称关键词
     * @return 品牌
     */
    IPage<Brand> selectByName(@Param("page") Page<?> page, @Param("keyword") String keyword);
}
