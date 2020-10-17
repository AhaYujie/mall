package online.ahayujie.mall.portal.pms.controller;


import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.pms.bean.dto.BrandDTO;
import online.ahayujie.mall.portal.pms.service.BrandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @ApiOperation(value = "分页获取商品品牌")
    @GetMapping("list")
    public Result<CommonPage<BrandDTO>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                             @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(brandService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "获取商品品牌详情")
    @GetMapping("detail")
    public Result<Object> getDetail(@RequestParam Long id) {
        return Result.data(brandService.getDetail(id));
    }
}
