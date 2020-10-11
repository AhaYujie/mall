package online.ahayujie.mall.portal.mms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.mms.bean.dto.CreateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.dto.ReceiveAddressDTO;
import online.ahayujie.mall.portal.mms.bean.dto.UpdateReceiveAddressParam;
import online.ahayujie.mall.portal.mms.bean.model.Member;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
import online.ahayujie.mall.portal.mms.mapper.ReceiveAddressMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.mms.service.ReceiveAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 会员收货地址表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-10
 */
@Service
public class ReceiveAddressServiceImpl implements ReceiveAddressService {
    private MemberService memberService;

    private final ReceiveAddressMapper receiveAddressMapper;

    public ReceiveAddressServiceImpl(ReceiveAddressMapper receiveAddressMapper) {
        this.receiveAddressMapper = receiveAddressMapper;
    }

    @Override
    public CommonPage<ReceiveAddressDTO> list(Long pageNum, Long pageSize) {
        Member member = memberService.getMemberFromToken();
        Page<ReceiveAddressDTO> page = new Page<>(pageNum, pageSize);
        Page<ReceiveAddressDTO> receiveAddressPage = receiveAddressMapper.selectPageByMemberId(page, member.getId());
        return new CommonPage<>(receiveAddressPage);
    }

    @Override
    public void create(CreateReceiveAddressParam param) {
        Member member = memberService.getMemberFromToken();
        ReceiveAddress receiveAddress = new ReceiveAddress();
        BeanUtils.copyProperties(param, receiveAddress);
        receiveAddress.setMemberId(member.getId());
        receiveAddress.setCreateTime(new Date());
        if (receiveAddress.getIsDefault() != null && receiveAddress.getIsDefault() == ReceiveAddress.DEFAULT) {
            updateOldDefaultReceiveAddress(member.getId());
        }
        receiveAddressMapper.insert(receiveAddress);
    }

    @Override
    public void update(UpdateReceiveAddressParam param) {
        if (receiveAddressMapper.selectById(param.getId()) == null) {
            throw new IllegalArgumentException("收货地址不存在");
        }
        Member member = memberService.getMemberFromToken();
        ReceiveAddress receiveAddress = new ReceiveAddress();
        BeanUtils.copyProperties(param, receiveAddress);
        receiveAddress.setUpdateTime(new Date());
        if (receiveAddress.getIsDefault() != null && receiveAddress.getIsDefault() == ReceiveAddress.DEFAULT) {
            updateOldDefaultReceiveAddress(member.getId());
        }
        receiveAddressMapper.updateById(receiveAddress);
    }

    @Override
    public void delete(Long id) {
        receiveAddressMapper.deleteById(id);
    }

    @Override
    public ReceiveAddressDTO getDefault() {
        Member member = memberService.getMemberFromToken();
        ReceiveAddress receiveAddress = receiveAddressMapper.selectDefaultByMemberId(member.getId(), ReceiveAddress.DEFAULT);
        if (receiveAddress == null) {
            return null;
        }
        ReceiveAddressDTO receiveAddressDTO = new ReceiveAddressDTO();
        BeanUtils.copyProperties(receiveAddress, receiveAddressDTO);
        return receiveAddressDTO;
    }

    /**
     * 更新旧的默认地址为非默认地址
     * @param memberId 会员id
     */
    private void updateOldDefaultReceiveAddress(Long memberId) {
        ReceiveAddress oldDefault = receiveAddressMapper.selectDefaultByMemberId(memberId, ReceiveAddress.DEFAULT);
        if (oldDefault != null) {
            ReceiveAddress update = new ReceiveAddress();
            update.setId(oldDefault.getId());
            update.setIsDefault(ReceiveAddress.NOT_DEFAULT);
            update.setUpdateTime(new Date());
            receiveAddressMapper.updateById(update);
        }
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }
}
