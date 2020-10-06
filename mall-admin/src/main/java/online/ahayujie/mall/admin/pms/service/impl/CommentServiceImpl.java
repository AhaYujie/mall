package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.mapper.CommentMapper;
import online.ahayujie.mall.admin.pms.service.CommentService;
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

    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Comment saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
        return comment;
    }
}
