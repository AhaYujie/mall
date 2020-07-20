package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.CreateProductParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductDTO;
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
}
