package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCategoryTree;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.List;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-10
 */
public interface ProductCategoryService {
    /**
     * 创建商品分类
     * @param param 商品分类信息
     * @throws IllegalProductCategoryException 上级分类编号不合法 或 isNav不合法 或 isShow不合法
     */
    void create(CreateProductCategoryParam param) throws IllegalProductCategoryException;

    /**
     * 更新商品分类信息。
     * 更新成功后，通过消息队列发送消息
     * @param id 商品分类id
     * @param param 商品分类信息
     * @throws IllegalProductCategoryException 商品分类不存在 或 上级分类编号不合法 或 isNav不合法 或 isShow不合法
     */
    void update(Long id, UpdateProductCategoryParam param) throws IllegalProductCategoryException;

    /**
     * 根据上级分类分页查询商品分类
     * @param parentId 上级分类id
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 商品分类
     */
    CommonPage<ProductCategory> getPageByParentId(Long parentId, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取商品分类，若不存在则返回null
     * @param id 商品分类id
     * @return 商品分类
     */
    ProductCategory getById(Long id);

    /**
     * 根据id删除商品分类。
     * 删除商品分类成功后，通过消息队列发送删除商品分类的消息
     * @param id 商品分类id
     */
    void delete(Long id);

    /**
     * 更新商品分类的导航栏显示状态。
     * 调用 {@link #update(Long, UpdateProductCategoryParam)} 更新每一个商品分类，
     * 若某一个商品分类不存在则忽略该分类
     * @see #update(Long, UpdateProductCategoryParam)
     * @param ids 商品分类id
     * @param isNav 商品分类导航栏显示状态
     * @throws IllegalProductCategoryException 导航栏显示状态不合法
     */
    void updateNavStatus(List<Long> ids, Integer isNav) throws IllegalProductCategoryException;

    /**
     * 更新商品分类的显示状态。
     * 调用 {@link #update(Long, UpdateProductCategoryParam)} 更新每一个商品分类，
     * 若某一个商品分类不存在则忽略该分类
     * @param ids 商品分类id
     * @param isShow 商品分类显示状态
     * @throws IllegalProductCategoryException 显示状态不合法
     */
    void updateShowStatus(List<Long> ids, Integer isShow) throws IllegalProductCategoryException;

    /**
     * 查询所有一级分类及子分类
     * @return 商品分类
     */
    List<ProductCategoryTree> listWithChildren();
}
