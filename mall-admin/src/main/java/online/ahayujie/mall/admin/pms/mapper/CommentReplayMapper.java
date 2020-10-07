package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.model.CommentReplay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品评价回复表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-07
 */
@Mapper
@Repository
public interface CommentReplayMapper extends BaseMapper<CommentReplay> {
    /**
     * 根据商品评价id分页查询
     * @param page 分页参数
     * @param commentId 商品评价id
     * @return 商品评价回复
     */
    Page<CommentReplay> selectPageByCommentId(@Param("page") Page<CommentReplay> page, @Param("commentId") Long commentId);
}
