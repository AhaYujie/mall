package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.ProductCategoryDTO;
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

    @ApiOperation(value = "获取所有一级分类")
    @GetMapping("first-level")
    public Result<List<ProductCategoryDTO>> getFirstLevel() {
        return Result.data(productCategoryService.getFirstLevel());
    }

    @ApiOperation(value = "分页获取二级分类")
    @GetMapping("second-level")
    public Result<CommonPage<ProductCategoryDTO>> getSecondLevel(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                                 @RequestParam(required = false, defaultValue = "20") Long pageSize,
                                                                 @RequestParam Long parentId) {
        return Result.data(productCategoryService.getSecondLevel(pageNum, pageSize, parentId));
    }
}
