package online.ahayujie.mall.admin.ums.mapper;

import online.ahayujie.mall.admin.ums.bean.model.AdminRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 后台用户和角色关系表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-08
 */
@Mapper
@Repository
public interface AdminRoleRelationMapper extends BaseMapper<AdminRoleRelation> {
    /**
     * 根据用户id查询
     * @param adminId 用户id
     * @return 用户角色关系
     */
    List<AdminRoleRelation> selectByAdminId(Long adminId);

    /**
     * 根据用户id删除
     * @param adminId 用户id
     * @return 删除数量
     */
    int deleteByAdminId(Long adminId);

    /**
     * 批量插入
     * @param adminRoleRelations 用户角色关系
     * @return 插入数量
     */
    int insert(@Param("list") List<AdminRoleRelation> adminRoleRelations);
}
