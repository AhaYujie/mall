package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.*;
import online.ahayujie.mall.admin.pms.service.ProductService;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.common.exception.ApiException;
import org.springframework.web.bind.annotation.*;

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
}
