package online.ahayujie.mall.admin.pms.mapper;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class BrandMapperTest {
    @Autowired
    private BrandMapper brandMapper;

    @Test
    void selectAll() {
        List<Brand> oldBrands = brandMapper.selectAll();
        Random random = new Random();
        int size = random.nextInt(20);
        List<Brand> addBrands = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Brand brand = new Brand();
            brand.setName("for test");
            addBrands.add(brand);
        }
        addBrands.forEach(brandMapper::insert);
        List<Brand> newBrands = brandMapper.selectAll();
        assertEquals(oldBrands.size() + size, newBrands.size());
        log.debug("oldBrands: " + oldBrands);
        log.debug("newBrands: " + newBrands);
    }
}