package online.ahayujie.mall.admin.ums.service.impl;

import online.ahayujie.mall.admin.ums.bean.model.AdminRoleRelation;
import online.ahayujie.mall.admin.ums.bean.model.Role;
import online.ahayujie.mall.admin.ums.mapper.AdminRoleRelationMapper;
import online.ahayujie.mall.admin.ums.mapper.RoleMapper;
import online.ahayujie.mall.admin.ums.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMapper roleMapper;
    private final AdminRoleRelationMapper adminRoleRelationMapper;

    public RoleServiceImpl(RoleMapper roleMapper, AdminRoleRelationMapper adminRoleRelationMapper) {
        this.roleMapper = roleMapper;
        this.adminRoleRelationMapper = adminRoleRelationMapper;
    }

    @Override
    public List<Role> getRoleListByAdminId(Long adminId) {
        List<AdminRoleRelation> adminRoleRelations = adminRoleRelationMapper.selectByAdminId(adminId);
        return adminRoleRelations.stream()
                .map(relation -> roleMapper.selectById(relation.getRoleId()))
                .collect(Collectors.toList());
    }
}
