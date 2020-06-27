package online.ahayujie.mall.admin.ums.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Mapper
@Repository
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名查询
     * @param username 用户名
     * @return 用户
     */
    Admin selectByUsername(String username);

    /**
     * 根据用户名或昵称查询后台用户
     * @param page 分页
     * @param keyword 关键词
     * @return 后台用户
     */
    IPage<Admin> selectByUsernameAndNickName(@Param("page") Page<?> page, @Param("keyword") String keyword);
}
