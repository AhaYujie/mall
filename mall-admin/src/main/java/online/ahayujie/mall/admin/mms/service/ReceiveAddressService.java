package online.ahayujie.mall.admin.mms.service;

import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;

/**
 * <p>
 * 会员收货地址表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-22
 */
public interface ReceiveAddressService {
    /**
     * 获取会员的默认收货地址。
     * 如果不存在则返回null。
     * @param memberId 会员id
     * @return 会员的默认收货地址
     */
    ReceiveAddress getDefaultReceiveAddress(Long memberId);
}
