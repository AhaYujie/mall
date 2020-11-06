package online.ahayujie.mall.search.controller;

import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.search.bean.dto.QueryProductParam;
import online.ahayujie.mall.search.bean.dto.RecommendProductParam;
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

    @PostMapping("/search/simple")
    @ApiOperation(value = "简单搜索", notes = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；" +
            "3->按价格从低到高排序")
    public Result<CommonPage<EsProduct>> search(@RequestBody SimpleQueryProductParam param) {
        Integer pageNum = param.getPageNum(), pageSize = param.getPageSize(), sort = param.getSort();
        pageNum = ((pageNum == null || pageNum < 1) ? 1 : pageNum);
        pageSize = ((pageSize == null || pageSize < 1) ? 20 : pageSize);
        sort = ((sort == null || sort < 0 || sort > 3) ? 0 : sort);
        return Result.data(productService.search(pageNum, pageSize, param.getKeyword(), sort));
    }

    @PostMapping("/search")
    @ApiOperation(value = "搜索商品", notes = "sort：0->按相关度排序；1->按销量从高到低排序；2->按价格从高到低排序；" +
            "3->按价格从低到高排序")
    public Result<CommonPage<EsProduct>> search(@RequestBody QueryProductParam param) {
        Integer pageNum = param.getPageNum(), pageSize = param.getPageSize(), sort = param.getSort();
        param.setPageNum((pageNum == null || pageNum < 1) ? 1 : pageNum);
        param.setPageSize((pageSize == null || pageSize < 1) ? 20 : pageSize);
        param.setSort((sort == null || sort < 0 || sort > 3) ? 0 : sort);
        return Result.data(productService.search(param.getPageNum(), param.getPageSize(), param.getKeyword(),
                param.getBrandId(), param.getProductCategoryId(), param.getProductSn(), param.getIsPublish(),
                param.getIsNew(), param.getIsRecommend(), param.getIsVerify(), param.getIsPreview(),
                param.getMinPrice(), param.getMaxPrice(), param.getSort()));
    }

    @ApiOperation(value = "根据商品id获取推荐商品")
    @PostMapping("/recommend")
    public Result<CommonPage<EsProduct>> recommend(@RequestBody RecommendProductParam param) {
        Integer pageNum = param.getPageNum(), pageSize = param.getPageSize();
        pageNum = ((pageNum == null || pageNum < 1) ? 1 : pageNum);
        pageSize = ((pageSize == null || pageSize < 1) ? 20 : pageSize);
        return Result.data(productService.recommend(pageNum, pageSize, param.getId()));
    }
}
