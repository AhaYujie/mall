package online.ahayujie.mall.search.controller;

import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.search.bean.model.EsProduct;
import online.ahayujie.mall.search.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author aha
 * @since 2020/10/31
 */
@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<EsProduct> getById(@RequestParam Long id) {
        return Result.data(productService.getById(id));
    }

    @GetMapping("/search/simple")
    @ApiOperation(value = "简单搜索", notes = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；" +
            "3->按价格从低到高排序")
    public Result<CommonPage<EsProduct>> search(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false, defaultValue = "0") Integer sort) {
        return Result.data(productService.search(pageNum, pageSize, keyword, sort));
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索商品", notes = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；" +
            "3->按价格从低到高排序")
    public Result<CommonPage<EsProduct>> search(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                                @RequestParam(required = false) String keyword,
                                                @RequestParam(required = false) Long brandId,
                                                @RequestParam(required = false) Long productCategoryId,
                                                @RequestParam(required = false) String productSn,
                                                @RequestParam(required = false) Integer isPublish,
                                                @RequestParam(required = false) Integer isNew,
                                                @RequestParam(required = false) Integer isRecommend,
                                                @RequestParam(required = false) Integer isVerify,
                                                @RequestParam(required = false) Integer isPreview,
                                                @RequestParam(required = false) BigDecimal minPrice,
                                                @RequestParam(required = false) BigDecimal maxPrice,
                                                @RequestParam(required = false, defaultValue = "0") Integer sort) {
        return Result.data(productService.search(pageNum, pageSize, keyword, brandId, productCategoryId, productSn,
                isPublish, isNew, isRecommend, isVerify, isPreview, minPrice, maxPrice, sort));
    }
}
