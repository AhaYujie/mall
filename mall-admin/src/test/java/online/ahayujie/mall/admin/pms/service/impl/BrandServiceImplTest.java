package online.ahayujie.mall.admin.pms.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.CreateBrandParam;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateBrandParam;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import online.ahayujie.mall.admin.pms.exception.IllegalBrandException;
import online.ahayujie.mall.admin.pms.mapper.BrandMapper;
import online.ahayujie.mall.admin.pms.service.BrandService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.bean.model.Base;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BrandServiceImplTest {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private BrandService brandService;

    @Test
    void create() {
        // illegal brand
        CreateBrandParam param = new CreateBrandParam();
        param.setName("for test");
        param.setIsFactory(-1);
        param.setIsShow(Brand.ShowStatus.SHOW.getValue());
        param.setFirstLetter("F");
        CreateBrandParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalBrandException.class, () -> brandService.create(finalParam));
        log.debug(throwable1.getMessage());
        param = new CreateBrandParam();
        param.setName("for test");
        param.setIsFactory(Brand.FactoryStatus.FACTORY.getValue());
        param.setIsShow(-1);
        param.setFirstLetter("F");
        CreateBrandParam finalParam1 = param;
        Throwable throwable2 = assertThrows(IllegalBrandException.class, () -> brandService.create(finalParam1));
        log.debug(throwable2.getMessage());
        param = new CreateBrandParam();
        param.setName("for test");
        param.setIsFactory(Brand.FactoryStatus.FACTORY.getValue());
        param.setIsShow(Brand.ShowStatus.SHOW.getValue());
        param.setFirstLetter("FOR");
        CreateBrandParam finalParam2 = param;
        Throwable throwable3 = assertThrows(IllegalBrandException.class, () -> brandService.create(finalParam2));
        log.debug(throwable3.getMessage());

        // legal
        List<CreateBrandParam> params = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            param = new CreateBrandParam();
            param.setName("for test " + i);
            param.setIsFactory(Brand.FactoryStatus.FACTORY.getValue());
            param.setIsShow(Brand.ShowStatus.SHOW.getValue());
            param.setFirstLetter("F");
            params.add(param);
        }
        List<Brand> oldBrands = brandService.listAll();
        params.forEach(brandService::create);
        List<Brand> newBrands = brandService.listAll();
        log.debug("oldBrands: " + oldBrands);
        log.debug("newBrands: " + newBrands);
        assertEquals(oldBrands.size() + size, newBrands.size());
    }

    @Test
    void update() {
        Brand oldBrand = new Brand();
        oldBrand.setName("for test");
        brandMapper.insert(oldBrand);
        Long id = oldBrand.getId();

        // illegal
        UpdateBrandParam param = new UpdateBrandParam();
        param.setIsFactory(-1);
        UpdateBrandParam finalParam = param;
        Throwable throwable1 = assertThrows(IllegalBrandException.class, () -> brandService.update(id, finalParam));
        log.debug(throwable1.getMessage());
        param = new UpdateBrandParam();
        param.setIsShow(-1);
        UpdateBrandParam finalParam1 = param;
        Throwable throwable2 = assertThrows(IllegalBrandException.class, () -> brandService.update(id, finalParam1));
        log.debug(throwable2.getMessage());
        param = new UpdateBrandParam();
        param.setFirstLetter("ABC");
        UpdateBrandParam finalParam2 = param;
        Throwable throwable3 = assertThrows(IllegalBrandException.class, () -> brandService.update(id, finalParam2));
        log.debug(throwable3.getMessage());
        param = new UpdateBrandParam();
        param.setName("test");
        UpdateBrandParam finalParam3 = param;
        Throwable throwable4 = assertThrows(IllegalBrandException.class, () -> brandService.update(-1L, finalParam3));
        log.debug(throwable4.getMessage());

        // legal
        param = new UpdateBrandParam();
        param.setName("update name");
        param.setFirstLetter("N");
        param.setIsShow(Brand.ShowStatus.SHOW.getValue());
        param.setIsFactory(Brand.FactoryStatus.FACTORY.getValue());
        param.setBigPic("update pic");
        param.setBrandStory("update story");
        param.setLogo("update logo");
        param.setSort(1);
        brandService.update(id, param);
        Brand newBrand = brandMapper.selectById(id);
        assertNotEquals(oldBrand, newBrand);
        assertEquals(param.getName(), newBrand.getName());
        assertEquals(param.getFirstLetter(), newBrand.getFirstLetter());
        assertEquals(param.getIsFactory(), newBrand.getIsFactory());
        assertEquals(param.getIsShow(), newBrand.getIsShow());
        assertEquals(param.getBigPic(), newBrand.getBigPic());
        assertEquals(param.getBrandStory(), newBrand.getBrandStory());
        assertEquals(param.getLogo(), newBrand.getLogo());
        assertEquals(param.getSort(), newBrand.getSort());
        log.debug("newBrand: " + newBrand);
    }

    @Test
    void delete() {
        // illegal
        Long id = -1L;
        Throwable throwable = assertThrows(IllegalBrandException.class, () -> brandService.delete(id));
        log.debug(throwable.getMessage());

        // legal
        Random random = new Random();
        List<Brand> brands = new ArrayList<>();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test: " + i);
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        List<Brand> oldBrands = brandService.listAll();
        List<Long> brandIds = brands.stream().map(Base::getId).collect(Collectors.toList());
        brandIds.forEach(brandService::delete);
        List<Brand> newBrands = brandService.listAll();
        assertEquals(oldBrands.size() - size, newBrands.size());
        log.debug("oldBrands: " + oldBrands);
        log.debug("newBrands: " + newBrands);
    }

    @Test
    void list() {
        List<Brand> brands = new ArrayList<>();
        int size = 10;
        String name = "abcdefg";
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName(name);
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        int pageNum = 1, pageSize = 5;
        CommonPage<Brand> result2 = brandService.list(name, pageNum, pageSize);
        log.debug("result2: " + result2);
        assertEquals(pageSize, result2.getData().size());
        pageNum = 2;
        CommonPage<Brand> result3 = brandService.list(name, pageNum, pageSize);
        log.debug("result3: " + result3);
        assertEquals(size - pageSize, result3.getData().size());
    }

    @Test
    void deleteBatch() {
        // legal
        List<Brand> brands = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test: " + i);
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        List<Long> ids = brands.stream().map(Base::getId).collect(Collectors.toList());
        List<Brand> oldBrands = brandService.listAll();
        brandService.deleteBatch(ids);
        List<Brand> newBrands = brandService.listAll();
        log.debug("oldBrands: " + oldBrands);
        log.debug("newBrands: " + newBrands);
        assertEquals(oldBrands.size() - size, newBrands.size());

        // illegal
        brands = new ArrayList<>();
        size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test: " + i);
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        ids = brands.stream().map(Base::getId).collect(Collectors.toList());
        ids.add(-1L);
        oldBrands = brandService.listAll();
        List<Long> finalIds = ids;
        Throwable throwable1 = assertThrows(IllegalBrandException.class, () -> brandService.deleteBatch(finalIds));
        newBrands = brandService.listAll();
        log.debug(throwable1.getMessage());
        log.debug("oldBrands: " + oldBrands);
        log.debug("newBrands: " + newBrands);
        assertEquals(oldBrands.size(), newBrands.size());
    }

    @Test
    void updateShowStatus() {
        Random random = new Random();
        List<Brand> brands = new ArrayList<>();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test: " + i);
            brand.setIsShow(Brand.ShowStatus.NOT_SHOW.getValue());
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        List<Long> ids = brands.stream().map(Base::getId).collect(Collectors.toList());
        Integer isShow = Brand.ShowStatus.SHOW.getValue();
        brandService.updateShowStatus(ids, isShow);
        List<Brand> newBrands = brandMapper.selectBatchIds(ids);
        for (Brand brand : newBrands) {
            assertEquals(Brand.ShowStatus.SHOW.getValue(), brand.getIsShow());
        }

        // illegal
        Throwable throwable = assertThrows(IllegalBrandException.class, () -> brandService.updateShowStatus(ids, -1));
        log.debug(throwable.getMessage());
    }

    @Test
    void updateFactoryStatus() {
        Random random = new Random();
        List<Brand> brands = new ArrayList<>();
        int size = random.nextInt(20);
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test: " + i);
            brand.setIsFactory(Brand.FactoryStatus.NON_FACTORY.getValue());
            brands.add(brand);
        }
        brands.forEach(brandMapper::insert);
        List<Long> ids = brands.stream().map(Base::getId).collect(Collectors.toList());
        Integer isFactory = Brand.FactoryStatus.FACTORY.getValue();
        brandService.updateFactoryStatus(ids, isFactory);
        List<Brand> newBrands = brandMapper.selectBatchIds(ids);
        for (Brand brand : newBrands) {
            assertEquals(Brand.ShowStatus.SHOW.getValue(), brand.getIsShow());
        }

        // illegal
        Throwable throwable = assertThrows(IllegalBrandException.class, () -> brandService.updateFactoryStatus(ids, -1));
        log.debug(throwable.getMessage());
    }
}