package online.ahayujie.mall.portal.oms.controller;

import io.swagger.annotations.ApiOperation;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.common.api.Result;
import online.ahayujie.mall.portal.oms.bean.dto.AddCartProductParam;
import online.ahayujie.mall.portal.oms.bean.dto.CartProductDTO;
import online.ahayujie.mall.portal.oms.service.CartProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 购物车商品表 前端控制器
 * </p>
 *
 * @author aha
 * @since 2020-10-22
 */
@RestController
@RequestMapping("/cart-product")
public class CartProductController {
    private final CartProductService cartProductService;

    public CartProductController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @ApiOperation(value = "分页获取会员的购物车列表")
    @GetMapping("list")
    public Result<CommonPage<CartProductDTO>> list(@RequestParam(required = false, defaultValue = "1") Long pageNum,
                                                   @RequestParam(required = false, defaultValue = "20") Long pageSize) {
        return Result.data(cartProductService.list(pageNum, pageSize));
    }

    @ApiOperation(value = "添加商品到购物车", notes = "如果购物车已经存在该商品(sku)，则增加商品购买数量")
    @PostMapping("add")
    public Result<Object> add(@RequestBody AddCartProductParam param) {
        try {
            cartProductService.add(param);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "更新购物车中商品的数量")
    @PostMapping("/update/quantity")
    public Result<Object> updateQuantity(@RequestParam Long id, @RequestParam Integer quantity) {
        try {
            cartProductService.updateQuantity(id, quantity);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "更新购物车中商品的sku")
    @PostMapping("/update/sku")
    public Result<Object> updateSku(@RequestParam Long cartProductId, @RequestParam Long skuId) {
        try {
            cartProductService.updateSku(cartProductId, skuId);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "删除购物车中的商品")
    @PostMapping("delete")
    public Result<Object> delete(@RequestParam List<Long> ids) {
        cartProductService.delete(ids);
        return Result.success();
    }
}
