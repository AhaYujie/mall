package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDTO;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Brand;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
public interface BrandService {
    /**
     * 分页获取商品品牌。
     * 商品品牌的isShow = {@link Brand.ShowStatus#SHOW}，
     * 根据sort从大到小排序。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 商品品牌
     */
    CommonPage<BrandDTO> list(Long pageNum, Long pageSize);

    /**
     * 获取商品品牌详情。
     * 商品品牌的isShow = {@link Brand.ShowStatus#SHOW}。
     *
     * @param id 商品品牌id
     * @return 商品品牌
     */
    BrandDetailDTO getDetail(Long id);
}
