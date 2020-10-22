package online.ahayujie.mall.admin.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.pms.bean.dto.CommentReplyMsgDTO;
import online.ahayujie.mall.admin.pms.bean.dto.CommentReplyParam;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.bean.model.CommentReplay;
import online.ahayujie.mall.admin.pms.mapper.CommentMapper;
import online.ahayujie.mall.admin.pms.mapper.CommentReplayMapper;
import online.ahayujie.mall.admin.pms.publisher.CommentPublisher;
import online.ahayujie.mall.admin.pms.service.CommentService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 商品评价表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-10-06
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentPublisher commentPublisher;
    private final CommentReplayMapper commentReplayMapper;

    public CommentServiceImpl(CommentMapper commentMapper, CommentPublisher commentPublisher, CommentReplayMapper commentReplayMapper) {
        this.commentMapper = commentMapper;
        this.commentPublisher = commentPublisher;
        this.commentReplayMapper = commentReplayMapper;
    }

    @Override
    public Comment saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public CommonPage<Comment> list(Long pageNum, Long pageSize, Long productId) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        Page<Comment> commentPage = commentMapper.selectPageByProductId(page, productId);
        return new CommonPage<>(commentPage);
    }

    @Override
    public void updateCommentIsShow(Long commentId, Integer isShow) throws IllegalArgumentException {
        if (isShow != Comment.HIDE && isShow != Comment.SHOW) {
            throw new IllegalArgumentException("isShow参数不合法");
        }
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setIsShow(isShow);
        comment.setUpdateTime(new Date());
        commentMapper.updateById(comment);
    }

    @Override
    public CommonPage<CommentReplay> replyList(Long pageNum, Long pageSize, Long commentId) {
        Page<CommentReplay> page = new Page<>(pageNum, pageSize);
        Page<CommentReplay> commentReplayPage = commentReplayMapper.selectPageByCommentId(page, commentId);
        return new CommonPage<>(commentReplayPage);
    }

    @Override
    public void replyComment(CommentReplyParam param) throws IllegalArgumentException {
        Comment comment = commentMapper.selectById(param.getCommentId());
        if (comment == null) {
            throw new IllegalArgumentException("商品评价不存在");
        }
        Comment update = new Comment();
        update.setId(param.getCommentId());
        update.setReplayCount(comment.getReplayCount() + 1);
        commentMapper.updateById(update);
        CommentReplay commentReplay = new CommentReplay();
        BeanUtils.copyProperties(param, commentReplay);
        commentReplay.setCreateTime(new Date());
        commentReplay.setType(CommentReplay.Type.ADMIN.getValue());
        commentReplayMapper.insert(commentReplay);
        CommentReplyMsgDTO msgDTO = new CommentReplyMsgDTO(param.getCommentId(), commentReplay.getId(), commentReplay.getContent());
        commentPublisher.publishCommentReplyMsg(msgDTO);
    }
}
