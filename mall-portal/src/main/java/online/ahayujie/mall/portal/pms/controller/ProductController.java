package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.ProductDetailDTO;
import online.ahayujie.mall.portal.pms.bean.dto.SkuDTO;
import online.ahayujie.mall.portal.pms.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品信息 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-17
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "获取商品详情")
    @GetMapping("detail")
    public Result<ProductDetailDTO> getDetail(@RequestParam Long id,
                                              @RequestParam(required = false, defaultValue = "0") Integer isMobile) {
        return Result.data(productService.getDetail(id, isMobile));
    }

    @ApiOperation(value = "获取sku的图片")
    @GetMapping("/sku/image")
    public Result<List<String>> getSkuImages(@RequestParam Long skuId) {
        return Result.data(productService.getSkuImages(skuId));
    }

    @ApiOperation(value = "获取商品的sku")
    @GetMapping("sku")
    public Result<SkuDTO> getSku(@RequestParam Long id) {
        return Result.data(productService.getSku(id));
    }
}
