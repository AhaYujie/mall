package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.CreateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.dto.DeleteProductCategoryMessageDTO;
import online.ahayujie.mall.admin.pms.bean.dto.ProductCategoryTree;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateProductCategoryParam;
import online.ahayujie.mall.admin.pms.bean.model.ProductCategory;
import online.ahayujie.mall.admin.pms.exception.IllegalProductCategoryException;
import online.ahayujie.mall.admin.pms.publisher.ProductCategoryPublisher;
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
     * 创建商品分类，支持多级分类。
     *
     * @param param 商品分类信息
     * @throws IllegalProductCategoryException 上级分类不合法 或 isNav不合法
     */
    void create(CreateProductCategoryParam param) throws IllegalProductCategoryException;

    /**
     * 更新商品分类信息，支持多级分类。
     * 上级分类不能是自身和所有下级分类(包括下一级分类的下级分类)。
     * 更新成功后，调用
     * {@link ProductCategoryPublisher#publishUpdateMsg(Long)}
     * 发送消息。
     *
     * @see ProductCategoryPublisher#publishUpdateMsg(Long)
     * @param id 商品分类id
     * @param param 商品分类信息
     * @throws IllegalProductCategoryException 商品分类不存在 或 上级分类不合法 或 isNav不合法
     */
    void update(Long id, UpdateProductCategoryParam param) throws IllegalProductCategoryException;

    /**
     * 根据上级分类分页查询该上级分类的下一级商品分类，不包括下一级分类的下级分类。
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
     * 根据id删除商品分类，同时递归删除该分类的下级分类。
     * 删除商品分类成功后，调用
     * {@link ProductCategoryPublisher#publishDeleteMsg(DeleteProductCategoryMessageDTO)}
     * 发送消息。
     *
     * @see ProductCategoryPublisher#publishDeleteMsg(DeleteProductCategoryMessageDTO)
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
     * 根据上级分类树形结构递归查询所有子分类。
     * 查询包括该上级分类的所有子分类及其子分类。
     * 例如上级分类A，有下一级子分类a，b，c；
     * 分类a有下一级子分类i, ii, iii；
     * 查询结果包括a，b，c，i，ii，iii。
     *
     * @param parentId 上级分类id
     * @return 商品分类
     */
    List<ProductCategoryTree> listWithChildren(Long parentId);
}
