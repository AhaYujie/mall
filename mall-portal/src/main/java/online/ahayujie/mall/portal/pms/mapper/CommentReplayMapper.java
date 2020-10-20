package online.ahayujie.mall.portal.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.model.CommentReplay;
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
 * @since 2020-10-19
 */
@Mapper
@Repository
public interface CommentReplayMapper extends BaseMapper<CommentReplay> {
    /**
     * 分页查询
     * @param page 分页参数
     * @param commentId 商品评价id
     * @return 商品评价回复
     */
    Page<CommentReplayDTO> selectByPage(@Param("page") Page<CommentReplayDTO> page, @Param("commentId") Long commentId);
}
