package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryTreeDTO;
import online.ahayujie.mall.portal.pms.service.ProductCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品分类 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@RestController
@RequestMapping("/product-category")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @ApiOperation(value = "分页获取导航栏商品分类")
    @GetMapping("nav")
    public Result<CommonPage<ProductCategoryDTO>> getNavProductCategory(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                                        @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        return Result.data(productCategoryService.getNavProductCategory(pageNum, pageSize));
    }

    @ApiOperation(value = "根据上级分类递归获取树形结构的商品分类", notes = "获取上级分类的所有下级分类, 例如包括下级分类A的下级分类a")
    @GetMapping("tree-list")
    public Result<List<ProductCategoryTreeDTO>> getTreeList(@RequestParam Long parentId) {
        return Result.data(productCategoryService.getTreeList(parentId));
    }
}
