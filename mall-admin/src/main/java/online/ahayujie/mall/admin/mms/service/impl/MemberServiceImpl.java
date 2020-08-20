package online.ahayujie.mall.admin.mms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-20
 */
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    public CommonPage<Member> list(Long pageNum, Long pageSize) {
        List<Member> members = memberMapper.selectByPage((pageNum - 1) * pageSize, pageSize);
        Long total = Long.valueOf(memberMapper.selectCount(Wrappers.emptyWrapper()));
        return new CommonPage<>(pageNum, pageSize, total / pageSize + 1, total, members);
    }

    @Override
    public CommonPage<Member> queryByUsername(Long pageNum, Long pageSize, String username) {
        List<Member> members = memberMapper.queryByUsername((pageNum - 1) * pageSize, pageSize, username);
        Long total = memberMapper.selectCountByUsername(username);
        return new CommonPage<>(pageNum, pageSize, total / pageSize + 1, total, members);
    }

    @Override
    public CommonPage<Member> queryByPhone(Long pageNum, Long pageSize, String phone) {
        List<Member> members = memberMapper.queryByPhone((pageNum - 1) * pageSize, pageSize, phone);
        Long total = memberMapper.selectCountByPhone(phone);
        return new CommonPage<>(pageNum, pageSize, total / pageSize + 1, total, members);
    }
}
