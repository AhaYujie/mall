package online.ahayujie.mall.portal.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.model.Comment;
import online.ahayujie.mall.portal.pms.mapper.CommentMapper;
import online.ahayujie.mall.portal.pms.mapper.CommentReplayMapper;
import online.ahayujie.mall.portal.pms.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-19
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentReplayMapper commentReplayMapper;

    public CommentServiceImpl(CommentMapper commentMapper, CommentReplayMapper commentReplayMapper) {
        this.commentMapper = commentMapper;
        this.commentReplayMapper = commentReplayMapper;
    }

    @Override
    public CommonPage<CommentDTO> list(Long productId, Long pageNum, Long pageSize, Integer category, Integer sort)
            throws IllegalArgumentException {
        if (sort != 0 && sort != 1) {
            throw new IllegalArgumentException("sort不合法");
        }
        Page<CommentDTO> page = new Page<>(pageNum, pageSize);
        Page<CommentDTO> commentDTOPage;
        switch (category) {
            case 0:
                commentDTOPage = commentMapper.selectByPage(page, productId, sort, Comment.SHOW);
                break;
            case 1:
                commentDTOPage = commentMapper.selectPageByStar(page, productId, sort, Comment.SHOW, Comment.Star.GOOD.value());
                break;
            case 2:
                commentDTOPage = commentMapper.selectPageByStar(page, productId, sort, Comment.SHOW, Comment.Star.NORMAL.value());
                break;
            case 3:
                commentDTOPage = commentMapper.selectPageByStar(page, productId, sort, Comment.SHOW, Comment.Star.BAD.value());
                break;
            case 4:
                commentDTOPage = commentMapper.selectPageByIsPic(page, productId, sort, Comment.SHOW, Comment.HAVE_PIC);
                break;
            default:
                throw new IllegalArgumentException("category不合法");
        }
        return new CommonPage<>(commentDTOPage);
    }

    @Override
    public CommonPage<CommentReplayDTO> replyList(Long commentId, Long pageNum, Long pageSize) {
        Page<CommentReplayDTO> page = new Page<>(pageNum, pageSize);
        Page<CommentReplayDTO> commentReplayDTOPage = commentReplayMapper.selectByPage(page, commentId);
        return new CommonPage<>(commentReplayDTOPage);
    }
}
