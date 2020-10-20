package online.ahayujie.mall.portal.pms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.model.Comment;
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
 * @since 2020-10-19
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 分页查询。
     *
     * @param page 分页参数
     * @param productId 商品id
     * @param sort 0->按照like_count排序，1->按照create_time从新到旧排序
     * @param isShow 是否显示
     * @return 商品评价
     */
    Page<CommentDTO> selectByPage(@Param("page") Page<CommentDTO> page, @Param("productId") Long productId,
                                  @Param("sort") Integer sort, @Param("isShow") Integer isShow);

    /**
     * 根据star分页查询
     *
     * @param page 分页参数
     * @param productId 商品id
     * @param sort 0->按照like_count排序，1->按照create_time从新到旧排序
     * @param isShow 是否显示
     * @param star 评价星数：0->5
     * @return 商品评价
     */
    Page<CommentDTO> selectPageByStar(@Param("page") Page<CommentDTO> page, @Param("productId") Long productId,
                                      @Param("sort") Integer sort, @Param("isShow") Integer isShow,
                                      @Param("star") Integer star);

    /**
     * 根据isPic分页查询
     *
     * @param page 分页参数
     * @param productId 商品id
     * @param sort 0->按照like_count排序，1->按照create_time从新到旧排序
     * @param isShow 是否显示
     * @param isPic 是否有图
     * @return 商品评价
     */
    Page<CommentDTO> selectPageByIsPic(@Param("page") Page<CommentDTO> page, @Param("productId") Long productId,
                                       @Param("sort") Integer sort, @Param("isShow") Integer isShow,
                                       @Param("isPic") Integer isPic);
}
