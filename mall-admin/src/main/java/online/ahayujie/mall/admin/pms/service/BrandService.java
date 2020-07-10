package online.ahayujie.mall.admin.pms.service;

import online.ahayujie.mall.admin.pms.bean.dto.CreateBrandParam;
import online.ahayujie.mall.admin.pms.bean.dto.UpdateBrandParam;
import online.ahayujie.mall.admin.pms.bean.model.Brand;
import online.ahayujie.mall.admin.pms.exception.IllegalBrandException;
import online.ahayujie.mall.common.api.CommonPage;

import java.util.List;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-07-08
 */
public interface BrandService {
    /**
     * 获取全部品牌
     * @return 全部品牌
     */
    List<Brand> listAll();

    /**
     * 创建品牌
     * @param param 品牌信息
     * @throws IllegalBrandException 品牌制造商状态不合法 或 品牌显示状态不合法 或 品牌首字母长度大于1
     */
    void create(CreateBrandParam param) throws IllegalBrandException;

    /**
     * 更新品牌信息，
     * 更新成功后，通过消息队列发送消息
     * @param id 品牌id
     * @param param 品牌信息
     * @throws IllegalBrandException 品牌不存在 或 品牌制造商状态不合法 或 品牌显示状态不合法 或 品牌首字母长度大于1
     */
    void update(Long id, UpdateBrandParam param) throws IllegalBrandException;

    /**
     * 删除品牌，
     * 删除成功后，通过消息队列发送消息
     * @param id 品牌id
     * @throws IllegalBrandException 品牌不存在
     */
    void delete(Long id) throws IllegalBrandException;

    /**
     * 根据品牌名称分页获取品牌列表
     * @param keyword 品牌名称关键词
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 品牌列表
     */
    CommonPage<Brand> list(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取品牌，若不存在则返回null
     * @param id 品牌id
     * @return 品牌
     */
    Brand getById(Long id);

    /**
     * 批量删除品牌，全部删除成功后，通过消息队列发送消息
     * 若其中一个品牌不存在删除失败，则全部回滚并且不发送消息
     * @param ids 品牌id
     * @throws IllegalBrandException 品牌不存在
     */
    void deleteBatch(List<Long> ids) throws IllegalBrandException;

    /**
     * 批量更新品牌的显示状态，若某个品牌不存在则忽略该品牌。
     * 调用 {@link #update(Long, UpdateBrandParam)} 更新每一个品牌显示状态
     * @see #update(Long, UpdateBrandParam)
     * @param ids 品牌id
     * @param isShow 品牌显示状态
     * @throws IllegalBrandException 品牌显示状态不合法
     */
    void updateShowStatus(List<Long> ids, Integer isShow) throws IllegalBrandException;

    /**
     * 批量更新品牌的制造商状态，若某个品牌不存在则忽略该品牌。
     * 调用 {@link #update(Long, UpdateBrandParam)} 更新每一个品牌制造商状态
     * @see #update(Long, UpdateBrandParam)
     * @param ids 品牌id
     * @param isFactory 品牌制造商状态
     * @throws IllegalBrandException 品牌制造商状态不合法
     */
    void updateFactoryStatus(List<Long> ids, Integer isFactory) throws IllegalBrandException;
}
