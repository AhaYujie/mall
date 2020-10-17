package online.ahayujie.mall.portal.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDTO;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Brand;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 品牌表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@Mapper
@Repository
public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 分页查询商品品牌。
     * 商品品牌的isShow = {@code isShow}，
     * 根据sort从大到小排序。
     *
     * @param page 分页参数
     * @param isShow 是否显示
     * @return 商品品牌
     */
    Page<BrandDTO> selectByPage(@Param("page") Page<BrandDTO> page, @Param("isShow") Integer isShow);

    /**
     * 根据id和isShow查询
     * @param id 商品品牌id
     * @param isShow 是否显示
     * @return 商品品牌
     */
    BrandDetailDTO selectByIdAndIsShow(@Param("id") Long id, @Param("isShow") Integer isShow);
}
