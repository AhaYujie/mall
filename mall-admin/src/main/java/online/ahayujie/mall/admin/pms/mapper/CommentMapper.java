package online.ahayujie.mall.admin.pms.mapper;

import online.ahayujie.mall.admin.pms.bean.model.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 商品评价表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-10-06
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

}
