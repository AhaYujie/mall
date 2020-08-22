package online.ahayujie.mall.admin.mms.service.impl;

import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.admin.mms.mapper.ReceiveAddressMapper;
import online.ahayujie.mall.admin.mms.service.ReceiveAddressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员收货地址表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-22
 */
@Service
public class ReceiveAddressServiceImpl implements ReceiveAddressService {
    private final ReceiveAddressMapper receiveAddressMapper;

    public ReceiveAddressServiceImpl(ReceiveAddressMapper receiveAddressMapper) {
        this.receiveAddressMapper = receiveAddressMapper;
    }

    @Override
    public ReceiveAddress getDefaultReceiveAddress(Long memberId) {
        return receiveAddressMapper.selectDefault(memberId, ReceiveAddress.DEFAULT);
    }
}
