package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.bean.model.Product;
import online.ahayujie.mall.admin.pms.bean.model.Sku;
import online.ahayujie.mall.admin.pms.exception.IllegalProductException;
import online.ahayujie.mall.admin.pms.service.ProductService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.common.exception.ApiException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "创建商品")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateProductParam param) {
        try {
            productService.create(param);
            return Result.success();
        } catch (ApiException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据商品id获取商品信息")
    @GetMapping("/info/{id}")
    public Result<ProductDTO> getProductById(@PathVariable Long id) {
        return Result.data(productService.getProductById(id));
    }

    @ApiOperation(value = "根据商品id更新商品信息")
    @PostMapping("/update/product/{id}")
    public Result<Object> updateProduct(@PathVariable Long id, @RequestBody UpdateProductParam param) {
        try {
            productService.updateProduct(id, param);
            return Result.success();
        } catch (ApiException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据商品id更新商品参数信息")
    @PostMapping("/update/param/{id}")
    public Result<Object> updateParam(@PathVariable Long id, @RequestBody UpdateProductParamParam param) {
        try {
            productService.updateParam(id, param);
            return Result.success();
        } catch (ApiException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据商品id更新商品规格信息", notes = "只能对已有的商品规格新增选项")
    @PostMapping("/update/specification/{id}")
    public Result<Object> updateSpecification(@PathVariable Long id, @RequestBody UpdateProductSpecificationParam param) {
        try {
            productService.updateSpecification(id, param);
            return Result.success();
        } catch (ApiException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据商品id更新商品sku信息", notes = "新增的sku不传id，更新的sku传id，不允许删除sku，更新的sku不允许更新与商品规格的关系")
    @PostMapping("/update/sku/{id}")
    public Result<Object> updateSku(@PathVariable Long id, @RequestBody UpdateSkuParam param) {
        try {
            productService.updateSku(id, param);
            return Result.success();
        } catch (ApiException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "分页获取商品列表", notes = "分页获取商品列表，根据排序字段和创建时间排序")
    @GetMapping("/list")
    public Result<CommonPage<Product>> list(@RequestParam(defaultValue = "1", required = false) Long pageNum,
                                            @RequestParam(defaultValue = "5", required = false) Long pageSize) {
        return Result.data(productService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "批量修改商品上下架状态")
    @PostMapping("/update/publishStatus")
    public Result<Object> updatePublishStatus(@RequestParam List<Long> ids, @RequestParam Integer publishStatus) {
        try {
            productService.updatePublishStatus(ids, publishStatus);
            return Result.success();
        } catch (IllegalProductException e) {
            return Result.fail("商品上下架状态不合法");
        }
    }

    @ApiOperation(value = "批量修改商品推荐状态")
    @PostMapping("/update/recommendStatus")
    public Result<Object> updateRecommendStatus(@RequestParam List<Long> ids, @RequestParam Integer recommendStatus) {
        try {
            productService.updateRecommendStatus(ids, recommendStatus);
            return Result.success();
        } catch (IllegalProductException e) {
            return Result.fail("商品推荐状态不合法");
        }
    }

    @ApiOperation(value = "批量修改商品新品状态")
    @PostMapping("/update/newStatus")
    public Result<Object> updateNewStatus(@RequestParam List<Long> ids, @RequestParam Integer newStatus) {
        try {
            productService.updateNewStatus(ids, newStatus);
            return Result.success();
        } catch (IllegalProductException e) {
            return Result.fail("商品新品状态不合法");
        }
    }

    @ApiOperation(value = "根据商品id和sku编号模糊搜索sku库存")
    @GetMapping("/sku/query/{id}")
    public Result<List<Sku>> querySku(@PathVariable Long id, @RequestParam(required = false) String keyword) {
        return Result.data(productService.querySku(id, keyword));
    }

    @ApiOperation(value = "审核商品")
    @PostMapping("/verify/{id}")
    public Result<Object> verifyProduct(@PathVariable Long id, @RequestParam Integer verifyStatus,
                                        @RequestParam String note) {
        try {
            productService.verifyProduct(id, verifyStatus, note);
            return Result.success();
        } catch (IllegalProductException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "分页查询商品", notes = "根据商品名称，货号，分类，品牌，上架状态，新品状态，推荐状态，审核状态，预告状态查询")
    @PostMapping("/query/{pageNum}/{pageSize}")
    public Result<CommonPage<Product>> query(@RequestBody QueryProductParam param,
                                             @PathVariable Long pageNum,
                                             @PathVariable Long pageSize) {
        return Result.data(productService.queryProduct(param, pageNum, pageSize));
    }
}
