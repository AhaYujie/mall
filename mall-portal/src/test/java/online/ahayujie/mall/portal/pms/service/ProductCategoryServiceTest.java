package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryTreeDTO;
import online.ahayujie.mall.portal.pms.bean.model.ProductCategory;
import online.ahayujie.mall.portal.pms.mapper.ProductCategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class ProductCategoryServiceTest extends TestBase {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductCategoryService productCategoryService;


    @Test
    void getNavProductCategory() {
        ProductCategory nav1 = new ProductCategory();
        nav1.setName("nav1");
        nav1.setParentId(ProductCategory.NON_PARENT_ID);
        nav1.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        productCategoryMapper.insert(nav1);
        ProductCategory nav2 = new ProductCategory();
        nav2.setName("nav2");
        nav2.setParentId(nav1.getId());
        nav2.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        productCategoryMapper.insert(nav2);
        ProductCategory nav3 = new ProductCategory();
        nav3.setName("nav3");
        nav3.setParentId(ProductCategory.NON_PARENT_ID);
        nav3.setIsNav(ProductCategory.NavStatus.SHOW.getValue());
        productCategoryMapper.insert(nav3);

        CommonPage<ProductCategoryDTO> result = productCategoryService.getNavProductCategory(1, 5);
        assertTrue(result.getData().size() >= 3);
        boolean f1 = false, f2 = false, f3 = false;
        for (ProductCategoryDTO productCategoryDTO : result.getData()) {
            if (productCategoryDTO.getId().equals(nav1.getId())) {
                f1 = true;
            } else if (productCategoryDTO.getId().equals(nav2.getId())) {
                f2 = true;
            } else if (productCategoryDTO.getId().equals(nav3.getId())) {
                f3 = true;
            }
        }
        assertTrue(f1);
        assertTrue(f2);
        assertTrue(f3);
    }

    @Test
    void getTreeList() {
        ProductCategory parent = new ProductCategory();
        parent.setName("parent");
        parent.setParentId(ProductCategory.NON_PARENT_ID);
        productCategoryMapper.insert(parent);
        ProductCategory child1 = new ProductCategory();
        child1.setName("child1");
        child1.setParentId(parent.getId());
        productCategoryMapper.insert(child1);
        ProductCategory child2 = new ProductCategory();
        child2.setName("child2");
        child2.setParentId(parent.getId());
        productCategoryMapper.insert(child2);
        ProductCategory subChild = new ProductCategory();
        subChild.setName("subChild");
        subChild.setParentId(child1.getId());
        productCategoryMapper.insert(subChild);
        ProductCategory subChild1 = new ProductCategory();
        subChild1.setName("subChild1");
        subChild1.setParentId(child2.getId());
        productCategoryMapper.insert(subChild1);
        ProductCategory subSub = new ProductCategory();
        subSub.setName("subSub");
        subSub.setParentId(subChild1.getId());
        productCategoryMapper.insert(subSub);

        List<ProductCategoryTreeDTO> trees = productCategoryService.getTreeList(parent.getId());
        assertEquals(2, trees.size());
        ProductCategoryTreeDTO tree1 = null, tree2 = null;
        for (ProductCategoryTreeDTO tree : trees) {
            if (tree.getProductCategory().getId().equals(child1.getId())) {
                tree1 = tree;
            } else if (tree.getProductCategory().getId().equals(child2.getId())) {
                tree2 = tree;
            }
        }
        assertNotNull(tree1);
        assertNotNull(tree2);
        assertEquals(1, tree1.getChildren().size());
        ProductCategoryTreeDTO subChildTree = tree1.getChildren().get(0);
        assertEquals(subChild.getId(), subChildTree.getProductCategory().getId());
        assertTrue(subChildTree.getChildren().isEmpty());
        ProductCategoryTreeDTO subChildTree1 = tree2.getChildren().get(0);
        assertEquals(subChild1.getId(), subChildTree1.getProductCategory().getId());
        assertEquals(1, subChildTree1.getChildren().size());
        assertEquals(subSub.getId(), subChildTree1.getChildren().get(0).getProductCategory().getId());
    }
}