package online.ahayujie.mall.admin.pms.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.pms.bean.dto.CreateBrandParam;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateBrandParam;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import online.ahayujie.mall.admin.pms.exception.IllegalBrandException;
import online.ahayujie.mall.admin.pms.service.BrandService;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品牌表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-07-08
 */
@RestController
@RequestMapping("/brand")
@Api(tags = "商品模块-商品品牌管理")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @ApiOperation(value = "获取全部品牌列表")
    @GetMapping("listAll")
    public Result<List<Brand>> listAll() {
        return Result.data(brandService.listAll());
    }

    @ApiOperation(value = "添加品牌")
    @PostMapping("create")
    public Result<Object> create(@RequestBody CreateBrandParam param) {
        try {
            brandService.create(param);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping("/update/{id}")
    public Result<Object> update(@PathVariable("id") Long id, @RequestBody UpdateBrandParam param) {
        try {
            brandService.update(id, param);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "删除品牌")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable("id") Long id) {
        try {
            brandService.delete(id);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping("list")
    public Result<CommonPage<Brand>> list(@RequestParam(required = false) String keyword,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "5") Integer pageSize) {
        return Result.data(brandService.list(keyword, pageNum, pageSize));
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping("/{id}")
    public Result<Brand> getById(@PathVariable("id") Long id) {
        return Result.data(brandService.getById(id));
    }

    @ApiOperation(value = "批量删除品牌")
    @PostMapping("/delete/batch")
    public Result<Object> deleteBatch(@RequestParam List<Long> ids) {
        try {
            brandService.deleteBatch(ids);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail(e.getResultCode(), e.getMessage());
        }
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping("/update/showStatus")
    public Result<Object> updateShowStatus(@RequestParam List<Long> ids, @RequestParam Integer isShow) {
        try {
            brandService.updateShowStatus(ids, isShow);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail("品牌显示状态不合法");
        }
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping("/update/factoryStatus")
    public Result<Object> updateFactoryStatus(@RequestParam List<Long> ids, @RequestParam Integer isFactory) {
        try {
            brandService.updateFactoryStatus(ids, isFactory);
            return Result.success();
        } catch (IllegalBrandException e) {
            return Result.fail("品牌制造商状态不合法");
        }
    }
}
