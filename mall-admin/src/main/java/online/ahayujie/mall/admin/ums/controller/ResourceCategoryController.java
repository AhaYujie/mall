package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceCategoryParam;
import online.ahayujie.mall.admin.ums.bean.model.ResourceCategory;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.service.ResourceCategoryService;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 资源分类表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/resourceCategory")
@Api(tags = "后台用户模块-资源管理")
public class ResourceCategoryController {
    private final ResourceCategoryService resourceCategoryService;

    public ResourceCategoryController(ResourceCategoryService resourceCategoryService) {
        this.resourceCategoryService = resourceCategoryService;
    }

    @ApiOperation(value = "查询所有资源分类")
    @GetMapping("/listAll")
    public Result<List<ResourceCategory>> listAll() {
        return Result.data(resourceCategoryService.listAll());
    }

    @ApiOperation(value = "添加后台资源分类")
    @PostMapping("/create")
    public Result<Object> create(@RequestBody CreateResourceCategoryParam param) {
        resourceCategoryService.create(param);
        return Result.success();
    }

    @ApiOperation(value = "修改后台资源分类")
    @PostMapping("/update/{id}")
    public Result<Object> update(@PathVariable Long id, @RequestBody UpdateResourceCategoryParam param) {
        try {
            resourceCategoryService.update(id, param);
            return Result.success();
        } catch (IllegalResourceCategoryException e) {
            return Result.fail("资源分类不存在");
        }
    }

    @ApiOperation(value = "根据ID删除后台资源分类", notes = "根据ID删除后台资源分类，只删除资源分类，不删除资源分类下的资源")
    @PostMapping("/delete/{id}")
    public Result<Object> delete(@PathVariable Long id) {
        resourceCategoryService.delete(id);
        return Result.success();
    }
}
