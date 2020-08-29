package online.ahayujie.mall.admin.oms.service;

import online.ahayujie.mall.admin.oms.bean.dto.CreateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.common.api.CommonPage;

/**
 * <p>
 * 公司收发货地址表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
public interface CompanyAddressService {
    /**
     * 新增公司地址。
     * 若默认收发货地址已经存在，且新增的地址也为默认则抛出
     * {@link IllegalCompanyAddressException} 异常。
     * @param param 公司地址信息
     * @throws IllegalCompanyAddressException 公司地址信息不合法
     */
    void create(CreateCompanyAddressParam param) throws IllegalCompanyAddressException;

    /**
     * 更新公司地址。
     * 若默认收发货地址已经存在，且新增的地址也为默认则抛出
     * {@link IllegalCompanyAddressException} 异常。
     * @param id 地址id
     * @param param 公司地址信息
     * @throws IllegalCompanyAddressException 地址不存在或地址信息不合法
     */
    void update(Long id, UpdateCompanyAddressParam param) throws IllegalCompanyAddressException;

    /**
     * 删除公司地址。
     * 若地址不存在则忽略不做操作。
     * @param id 公司地址id
     */
    void delete(Long id);

    /**
     * 分页获取公司地址。
     * 按照默认发货地址状态(默认在前)，默认收货地址状态(默认在前)，启用状态(启用在前)，创建时间(从新到旧)排序。
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 公司地址
     */
    CommonPage<CompanyAddress> list(Integer pageNum, Integer pageSize);

    /**
     * 根据id获取公司地址，如果不存在则返回null。
     * @param id 公司地址id
     * @return 公司地址
     */
    CompanyAddress getById(Long id);
}
