package online.ahayujie.mall.search.controller;

import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.search.bean.model.EsProduct;
import online.ahayujie.mall.search.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
