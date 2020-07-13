package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCategoryTree;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.service.ProductCategoryService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品分类 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-07-10
 */
@RestController
@RequestMapping("/product-category")
@Api(tags = "商品模块", value = "商品分类管理")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @ApiOperation(value = "添加商品分类")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateProductCategoryParam param) {
        try {
            productCategoryService.create(param);
            return Result.success();
        } catch (IllegalProductCategoryException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "修改商品分类")
    @PostMapping("/update/{id}")
    public Result<Object> update(@PathVariable Long id, @RequestBody UpdateProductCategoryParam param) {
        try {
            productCategoryService.update(id, param);
            return Result.success();
        } catch (IllegalProductCategoryException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据上级分类分页查询商品分类")
    @GetMapping("/list/{parentId}")
    public Result<CommonPage<ProductCategory>> getPageByParentId(@PathVariable Long parentId,
                                                                 @RequestParam(defaultValue = "5") Integer pageNum,
                                                                 @RequestParam(defaultValue = "1") Integer pageSize) {
        return Result.data(productCategoryService.getPageByParentId(parentId, pageNum, pageSize));
    }

    @ApiOperation(value = "根据id获取商品分类")
    @GetMapping("/{id}")
    public Result<ProductCategory> getById(@PathVariable Long id) {
        return Result.data(productCategoryService.getById(id));
    }

    @ApiOperation(value = "删除商品分类", notes = "删除商品分类，该分类下的商品不被删除，而是变成没有分类的商品")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        productCategoryService.delete(id);
        return Result.success();
    }

    @ApiOperation(value = "修改导航栏显示状态", notes = "修改导航栏显示状态，若某个商品分类不存在则忽略")
    @PostMapping("/update/navStatus")
    public Result<Object> updateNavStatus(@RequestParam List<Long> ids, @RequestParam Integer isNav) {
        try {
            productCategoryService.updateNavStatus(ids, isNav);
            return Result.success();
        } catch (IllegalProductCategoryException e) {
            return Result.fail("导航栏显示状态不合法");
        }
    }

    @ApiOperation(value = "修改显示状态", notes = "修改显示状态，若某个商品分类不存在则忽略")
    @PostMapping("/update/showStatus")
    public Result<Object> updateShowStatus(@RequestParam List<Long> ids, @RequestParam Integer isShow) {
        try {
            productCategoryService.updateShowStatus(ids, isShow);
            return Result.success();
        } catch (IllegalProductCategoryException e) {
            return Result.fail("显示状态不合法");
        }
    }

    @ApiOperation(value = "查询所有一级分类及子分类")
    @GetMapping("/list/withChildren")
    public Result<List<ProductCategoryTree>> listWithChildren() {
        return Result.data(productCategoryService.listWithChildren());
    }
}
