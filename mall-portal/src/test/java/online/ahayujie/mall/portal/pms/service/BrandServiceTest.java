package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDTO;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDetailDTO;
import online.ahayujie.mall.portal.pms.bean.model.Brand;
import online.ahayujie.mall.portal.pms.mapper.BrandMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BrandServiceTest extends TestBase {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private BrandService brandService;

    @Test
    void list() {
         for (int i = 0; i < 10; i++) {
             Brand brand = new Brand();
             brand.setName(getRandomString(4) + i);
             brand.setSort(i);
             brand.setIsShow(Brand.ShowStatus.SHOW.getValue());
             brandMapper.insert(brand);
         }
         CommonPage<BrandDTO> page = brandService.list(1L, 10L);
         assertEquals(10, page.getData().size());
         List<Brand> brands = page.getData().stream().map(dto -> brandMapper.selectById(dto.getId())).collect(Collectors.toList());
         Integer sort = Integer.MAX_VALUE;
         for (Brand brand : brands) {
             assertTrue(sort >= brand.getSort());
             sort = brand.getSort();
             assertEquals(Brand.ShowStatus.SHOW.getValue(), brand.getIsShow());
         }
    }

    @Test
    void getDetail() {
        // not exist
        assertNull(brandService.getDetail(-1L));

        // exist
        Brand brand = new Brand();
        brand.setName(getRandomString(6));
        brand.setIsShow(Brand.ShowStatus.SHOW.getValue());
        brandMapper.insert(brand);
        BrandDetailDTO brandDetailDTO = brandService.getDetail(brand.getId());
        assertEquals(brand.getName(), brandDetailDTO.getName());
    }
}