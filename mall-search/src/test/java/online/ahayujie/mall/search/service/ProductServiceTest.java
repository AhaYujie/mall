package online.ahayujie.mall.search.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.bean.model.Base;
import online.ahayujie.mall.search.bean.model.*;
import online.ahayujie.mall.search.mapper.ProductMapper;
import online.ahayujie.mall.search.mapper.ProductParamMapper;
import online.ahayujie.mall.search.mapper.ProductSpecificationMapper;
import online.ahayujie.mall.search.mapper.ProductSpecificationValueMapper;
import online.ahayujie.mall.search.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductParamMapper productParamMapper;
    @Autowired
    private ProductSpecificationMapper productSpecificationMapper;
    @Autowired
    private ProductSpecificationValueMapper productSpecificationValueMapper;

    @Test
    void saveEsProduct() {
        // null
        productService.saveEsProduct(-1L);
        EsProduct esProduct = productRepository.findById(-1L).orElse(null);
        assertNull(esProduct);

        // not null
        Product product = new Product();
        product.setName("for test");
        productMapper.insert(product);
        List<ProductParam> productParams = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductParam productParam = new ProductParam();
            productParam.setProductId(product.getId());
            productParam.setName("for test: " + i);
            if (i % 2 == 0) {
                productParam.setValue("{\"name\": \"test\"}");
                productParam.setType(ProductParam.Type.TEXT.getValue());
            } else {
                productParam.setValue("{\"name\": \"test\", \"image\": \"http://aha.jpg\"}");
                productParam.setType(ProductParam.Type.IMAGE_TEXT.getValue());
            }
            productParamMapper.insert(productParam);
            productParams.add(productParam);
        }
        List<ProductSpecification> specifications = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductSpecification specification = new ProductSpecification();
            specification.setProductId(product.getId());
            specification.setName("for test: " + i);
            productSpecificationMapper.insert(specification);
            for (int j = 0; j < 2; j++) {
                ProductSpecificationValue value = new ProductSpecificationValue();
                value.setProductSpecificationId(specification.getId());
                if (j % 2 == 0) {
                    value.setValue("{\"name\": \"test\"}");
                    value.setType(ProductSpecificationValue.Type.TEXT.getValue());
                } else {
                    value.setValue("{\"name\": \"test\", \"image\": \"http://aha.jpg\"}");
                    value.setType(ProductSpecificationValue.Type.IMAGE_TEXT.getValue());
                }
                productSpecificationValueMapper.insert(value);
            }
            specifications.add(specification);
        }
        productService.saveEsProduct(product.getId());
        EsProduct esProduct1 = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(esProduct1);
        assertEquals(product.getName(), esProduct1.getName());
        for (ProductParam productParam : productParams) {
            assertTrue(esProduct1.getParams().contains(productParam.getName()));
        }
        for (ProductSpecification specification : specifications) {
            assertTrue(esProduct1.getSpecifications().contains(specification.getName()));
        }
    }

    @Test
    void saveEsProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setName("for test: " + i);
            productMapper.insert(product);
            products.add(product);
        }
        productService.saveEsProducts(products.stream().map(Base::getId).collect(Collectors.toList()));
        for (Product product : products) {
            EsProduct esProduct = productRepository.findById(product.getId()).orElse(null);
            assertNotNull(esProduct);
            assertEquals(product.getName(), esProduct.getName());
        }
    }

    @Test
    void updateEsProduct() {
        Product product = new Product();
        product.setName("for test");
        productMapper.insert(product);
        productService.saveEsProduct(product.getId());
        product.setName("update name");
        productMapper.updateById(product);
        productService.updateEsProduct(product.getId());
        EsProduct esProduct = productRepository.findById(product.getId()).orElse(null);
        assertNotNull(esProduct);
        assertEquals(product.getName(), esProduct.getName());
    }

    @Test
    void updateEsProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setName("for test: " + i);
            productMapper.insert(product);
            products.add(product);
        }
        productService.saveEsProducts(products.stream().map(Base::getId).collect(Collectors.toList()));
        for (int i = 0; i < 10; i++) {
            products.get(i).setName("update: " + i);
            productMapper.updateById(products.get(i));
        }
        productService.updateEsProducts(products.stream().map(Base::getId).collect(Collectors.toList()));
        for (Product product : products) {
            EsProduct esProduct = productRepository.findById(product.getId()).orElse(null);
            assertNotNull(esProduct);
            assertEquals(product.getName(), esProduct.getName());
        }
    }
}