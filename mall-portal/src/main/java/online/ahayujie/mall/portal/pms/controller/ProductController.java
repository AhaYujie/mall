package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.*;
import online.ahayujie.mall.portal.pms.service.ProductService;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "搜索商品")
    @PostMapping("search")
    public Result<CommonPage<ProductDTO>> search(@RequestBody SearchProductParam param) {
        Integer pageNum = param.getPageNum(), pageSize = param.getPageSize(), sort = param.getSort();
        param.setPageNum((pageNum == null || pageNum < 1) ? 1 : pageNum);
        param.setPageSize((pageSize == null || pageSize < 1) ? 20 : pageSize);
        param.setSort((sort == null || sort < 0 || sort > 3) ? 0 : sort);
        return Result.data(productService.search(param));
    }

    @ApiOperation(value = "获取商品详情页的推荐商品")
    @PostMapping("recommend")
    public Result<CommonPage<ProductDTO>> recommend(@RequestBody RecommendProductParam param) {
        return Result.data(productService.recommend(param));
    }
}
