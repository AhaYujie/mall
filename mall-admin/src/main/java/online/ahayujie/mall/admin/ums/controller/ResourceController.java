package online.ahayujie.mall.admin.ums.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.admin.ums.bean.dto.CreateResourceParam;
import online.ahayujie.mall.admin.ums.bean.dto.UpdateResourceParam;
import online.ahayujie.mall.admin.ums.bean.model.Resource;
import online.ahayujie.mall.admin.ums.exception.IllegalResourceCategoryException;
import online.ahayujie.mall.admin.ums.service.ResourceService;
import online.ahayujie.mall.common.api.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台资源表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@RestController
@RequestMapping("/resource")
@Api(tags = "后台用户模块-资源管理")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @ApiOperation(value = "添加后台资源")
    @PostMapping("/create")
    public Result<Object> createResource(@RequestBody CreateResourceParam param) {
        try {
            resourceService.createResource(param);
            return Result.success();
        } catch (IllegalResourceCategoryException e) {
            return Result.fail("资源分类不存在");
        }
    }

    @ApiOperation(value = "修改后台资源")
    @PostMapping("/update/{id}")
    public Result<Object> updateResource(@PathVariable Long id, @RequestBody UpdateResourceParam param) {
        try {
            resourceService.updateResource(id, param);
            return Result.success();
        } catch (IllegalResourceCategoryException e) {
            return Result.fail("资源分类不存在");
        }
    }

    @ApiOperation(value = "根据ID获取资源详情")
    @GetMapping("/{id}")
    public Result<Resource> getResource(@PathVariable Long id) {
        return Result.data(resourceService.getById(id));
    }

    @ApiOperation(value = "根据ID删除后台资源")
    @PostMapping("/delete/{id}")
    public Result<Object> deleteResource(@PathVariable Long id) {
        resourceService.removeById(id);
        return Result.success();
    }

    @ApiOperation(value = "获取全部资源")
    @GetMapping("listAll")
    public Result<List<Resource>> listAll() {
        return Result.data(resourceService.list());
    }
}
