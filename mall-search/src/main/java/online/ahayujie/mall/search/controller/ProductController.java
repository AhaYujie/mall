package online.ahayujie.mall.search.controller;

import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.search.bean.dto.QueryProductParam;
import online.ahayujie.mall.search.bean.dto.SimpleQueryProductParam;
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
    public Result<CommonPage<EsProduct>> search(@RequestBody SimpleQueryProductParam param) {
        param.setPageNum(param.getPageNum() == null ? 1 : param.getPageNum());
        param.setPageSize(param.getPageSize() == null ? 20 : param.getPageSize());
        param.setSort(param.getSort() == null ? 0 : param.getSort());
        return Result.data(productService.search(param.getPageNum(), param.getPageSize(), param.getKeyword(), param.getSort()));
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索商品", notes = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；" +
            "3->按价格从低到高排序")
    public Result<CommonPage<EsProduct>> search(@RequestBody QueryProductParam param) {
        param.setPageNum(param.getPageNum() == null ? 1 : param.getPageNum());
        param.setPageSize(param.getPageSize() == null ? 20 : param.getPageSize());
        param.setSort(param.getSort() == null ? 0 : param.getSort());
        return Result.data(productService.search(param.getPageNum(), param.getPageSize(), param.getKeyword(),
                param.getBrandId(), param.getProductCategoryId(), param.getProductSn(), param.getIsPublish(),
                param.getIsNew(), param.getIsRecommend(), param.getIsVerify(), param.getIsPreview(),
                param.getMinPrice(), param.getMaxPrice(), param.getSort()));
    }

    @ApiOperation(value = "根据商品id获取推荐商品")
    @GetMapping("/recommend")
    public Result<CommonPage<EsProduct>> recommend(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                   @RequestParam(required = false, defaultValue = "5") Integer pageSize,
                                                   @RequestParam Long id) {
        return Result.data(productService.recommend(pageNum, pageSize, id));
    }
}
