package online.ahayujie.mall.admin.mms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import online.ahayujie.mall.admin.mms.bean.model.LoginLog;
import online.ahayujie.mall.admin.mms.bean.model.Member;
import online.ahayujie.mall.admin.mms.mapper.LoginLogMapper;
import online.ahayujie.mall.admin.mms.mapper.MemberMapper;
import online.ahayujie.mall.admin.mms.service.MemberService;
import online.ahayujie.mall.admin.oms.service.OrderService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LoginLogMapper loginLogMapper;

    public MemberServiceImpl(MemberMapper memberMapper, LoginLogMapper loginLogMapper) {
        this.memberMapper = memberMapper;
        this.loginLogMapper = loginLogMapper;
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

    @Override
    public CommonPage<LoginLog> getLoginLog(Long pageNum, Long pageSize, Long id) {
        List<LoginLog> loginLogs = loginLogMapper.selectPageByMemberId((pageNum - 1) * pageSize, pageSize, id);
        Long total = loginLogMapper.selectCountByMemberId(id);
        return new CommonPage<>(pageNum, pageSize, total / pageSize + 1, total, loginLogs);
    }

    @Override
    public Member getById(Long id) {
        return memberMapper.selectById(id);
    }

    @Override
    public void updateIntegration(Long id, Integer diff) {
        int count = memberMapper.updateIntegrationById(id, diff);
        if (count == 0) {
            throw new IllegalArgumentException("更新会员积分失败");
        }
    }
}
