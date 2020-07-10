package online.ahayujie.mall.admin.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.dto.CreateBrandParam;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateBrandParam;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import online.ahayujie.mall.admin.pms.exception.IllegalBrandException;
import online.ahayujie.mall.admin.pms.mapper.BrandMapper;
import online.ahayujie.mall.admin.pms.service.BrandService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-07-08
 */
@Service
public class BrandServiceImpl implements BrandService {
    private final BrandMapper brandMapper;

    public BrandServiceImpl(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Override
    public List<Brand> listAll() {
        return brandMapper.selectAll();
    }

    @Override
    public void create(CreateBrandParam param) throws IllegalBrandException {
        Brand brand = new Brand();
        BeanUtils.copyProperties(param, brand);
        validateBrand(brand);
        brand.setCreateTime(new Date());
        brandMapper.insert(brand);
    }

    @Override
    public void update(Long id, UpdateBrandParam param) throws IllegalBrandException {
        Brand oldBrand = brandMapper.selectById(id);
        if (oldBrand == null) {
            throw new IllegalBrandException("品牌不存在");
        }
        Brand brand = new Brand();
        brand.setId(id);
        BeanUtils.copyProperties(param, brand);
        validateBrand(brand);
        brand.setUpdateTime(new Date());
        brandMapper.updateById(brand);
        // TODO:通过消息队列发送更新品牌消息
    }

    @Override
    public void delete(Long id) throws IllegalBrandException {
        Brand brand = brandMapper.selectById(id);
        if (brand == null) {
            throw new IllegalBrandException("品牌不存在");
        }
        brandMapper.deleteById(id);
        // TODO:通过消息队列发送删除品牌消息
    }

    @Override
    public CommonPage<Brand> list(String keyword, Integer pageNum, Integer pageSize) {
        Page<Brand> page = new Page<>(pageNum, pageSize);
        IPage<Brand> brandPage = brandMapper.selectByName(page, keyword);
        return new CommonPage<>(brandPage);
    }

    @Override
    public Brand getById(Long id) {
        return brandMapper.selectById(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) throws IllegalBrandException {
        List<Brand> brands = new ArrayList<>();
        for (Long id : ids) {
            Brand brand = brandMapper.selectById(id);
            if (brand == null) {
                throw new IllegalBrandException("品牌不存在: " + id);
            }
            brands.add(brand);
        }
        ids.forEach(brandMapper::deleteById);
        // TODO:通过消息队列发送批量删除品牌消息
    }

    @Override
    public void updateShowStatus(List<Long> ids, Integer isShow) throws IllegalBrandException {
        Brand brand = new Brand();
        brand.setIsShow(isShow);
        validateBrand(brand);
        for (Long id : ids) {
            UpdateBrandParam param = new UpdateBrandParam();
            param.setIsShow(isShow);
            try {
                update(id, param);
            } catch (IllegalBrandException e) {
                // do nothing
            }
        }
    }

    @Override
    public void updateFactoryStatus(List<Long> ids, Integer isFactory) throws IllegalBrandException {
        Brand brand = new Brand();
        brand.setIsShow(isFactory);
        validateBrand(brand);
        for (Long id : ids) {
            UpdateBrandParam param = new UpdateBrandParam();
            param.setIsShow(isFactory);
            try {
                update(id, param);
            } catch (IllegalBrandException e) {
                // do nothing
            }
        }
    }

    /**
     * 检查品牌信息合法性，若字段为null则不检查该字段
     * @param brand 品牌信息
     */
    private void validateBrand(Brand brand) throws IllegalBrandException {
        Integer isFactory = brand.getIsFactory();
        if (isFactory != null && !Arrays.stream(Brand.FactoryStatus.values())
                .map(Brand.FactoryStatus::getValue)
                .collect(Collectors.toList())
                .contains(isFactory)) {
            throw new IllegalBrandException("品牌制造商状态不合法");
        }
        Integer isShow = brand.getIsShow();
        if (isShow != null && !Arrays.stream(Brand.ShowStatus.values())
                .map(Brand.ShowStatus::getValue)
                .collect(Collectors.toList())
                .contains(isShow)) {
            throw new IllegalBrandException("品牌显示状态不合法");
        }
        String firstLetter = brand.getFirstLetter();
        if (firstLetter != null && firstLetter.length() > 1) {
            throw new IllegalBrandException("品牌首字母长度大于1");
        }
    }
}
