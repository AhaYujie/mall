package online.ahayujie.mall.portal.mms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.mms.bean.dto.CreateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.dto.ReceiveAddressDTO;
import online.ahayujie.mall.portal.mms.bean.dto.UpdateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;

/**
 * <p>
 * 会员收货地址表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-10
 */
public interface ReceiveAddressService {
    /**
     * 分页获取会员的收货地址，默认地址在首位
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 收货地址
     */
    CommonPage<ReceiveAddressDTO> list(Long pageNum, Long pageSize);

    /**
     * 新增收货地址，如果设置新地址为默认地址且原本已经存在默认地址，则旧的默认地址变为非默认
     * @param param 收货地址参数
     */
    void create(CreateReceiveAddressParam param);

    /**
     * 更新收货地址，如果设置地址为默认地址且原本已经存在默认地址，则旧的默认地址变为非默认。
     *
     * @param param 收货地址参数
     * @throws IllegalArgumentException 收货地址不存在
     */
    void update(UpdateReceiveAddressParam param) throws IllegalArgumentException;

    /**
     * 删除收货地址
     * @param id 收货地址id
     */
    void delete(Long id);

    /**
     * 获取会员的默认收货地址，如果不存在则返回null
     * @return 默认收货地址
     */
    ReceiveAddressDTO getDefault();

    /**
     * 获取会员的收货地址。
     * 如果不存在则返回null。
     *
     * @param id 收货地址id
     * @param memberId 会员id
     * @return 收货地址
     */
    ReceiveAddress getById(Long id, Long memberId);
}
