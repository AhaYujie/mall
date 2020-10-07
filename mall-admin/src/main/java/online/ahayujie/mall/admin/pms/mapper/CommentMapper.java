package online.ahayujie.mall.admin.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
    /**
     * 根据商品id分页查询
     * @param page 分页参数
     * @param productId 商品id
     * @return 商品评价
     */
    Page<Comment> selectPageByProductId(@Param("page") Page<Comment> page, @Param("productId") Long productId);
}
