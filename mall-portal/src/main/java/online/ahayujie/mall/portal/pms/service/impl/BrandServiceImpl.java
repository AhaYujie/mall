package online.ahayujie.mall.portal.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDTO;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Brand;
import online.ahayujie.mall.portal.pms.mapper.BrandMapper;
import online.ahayujie.mall.portal.pms.service.BrandService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@Service
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Override
    public CommonPage<BrandDTO> list(Long pageNum, Long pageSize) {
        Page<BrandDTO> page = new Page<>(pageNum, pageSize);
        Page<BrandDTO> brandDTOPage = brandMapper.selectByPage(page, Brand.ShowStatus.SHOW.getValue());
        return new CommonPage<>(brandDTOPage);
    }

    @Override
    public BrandDetailDTO getDetail(Long id) {
        return brandMapper.selectByIdAndIsShow(id, Brand.ShowStatus.SHOW.getValue());
    }
}
