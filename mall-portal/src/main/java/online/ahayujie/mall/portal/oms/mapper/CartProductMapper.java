package online.ahayujie.mall.portal.oms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.oms.bean.dto.CartProductDTO;
import online.ahayujie.mall.portal.oms.bean.model.CartProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 购物车商品表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-22
 */
@Mapper
@Repository
public interface CartProductMapper extends BaseMapper<CartProduct> {
    /**
     * 根据会员id分页查询
     * @param page 分页参数
     * @param memberId 会员id
     * @return 购物车商品
     */
    Page<CartProductDTO> selectPageByMemberId(@Param("page") Page<CartProductDTO> page, @Param("memberId") Long memberId);

    /**
     * 根据memberId和skuId查询商品数量
     * @param memberId 会员id
     * @param skuId sku的id
     * @return 购物车商品id和quantity
     */
    CartProduct selectQuantityByMemberIdAndSkuId(@Param("memberId") Long memberId, @Param("skuId") Long skuId);

    /**
     * 根据id和memberId查询
     * @param id 购物车商品id
     * @param memberId 会员id
     * @return 购物车商品
     */
    CartProduct selectByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);

    /**
     * 根据id和memberId删除
     * @param ids 购物车商品id
     * @param memberId 会员id
     * @return 删除数量
     */
    Long deleteByIdAndMemberId(@Param("list") List<Long> ids, @Param("memberId") Long memberId);
}
