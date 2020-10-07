package online.ahayujie.mall.admin.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.admin.pms.bean.dto.CommentReplyParam;
import online.ahayujie.mall.admin.pms.bean.model.Comment;
import online.ahayujie.mall.admin.pms.bean.model.CommentReplay;
import online.ahayujie.mall.admin.pms.mapper.CommentMapper;
import online.ahayujie.mall.admin.pms.mapper.CommentReplayMapper;
import online.ahayujie.mall.common.api.CommonPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentReplayMapper commentReplayMapper;

    @Test
    void list() {
        // empty
        CommonPage<Comment> page = commentService.list(1L, 20L, -1L);
        assertEquals(0, page.getData().size());

        // not empty
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Comment comment = new Comment();
            comment.setProductId(1L);
            commentMapper.insert(comment);
            comments.add(comment);
        }
        CommonPage<Comment> page1 = commentService.list(1L, 10L, 1L);
        assertEquals(comments.size(), page1.getData().size());
    }

    @Test
    void updateCommentIsShow() {
        // illegal
        assertThrows(IllegalArgumentException.class, () -> commentService.updateCommentIsShow(-1L, -1));

        // legal
        Comment comment = new Comment();
        comment.setProductId(1L);
        commentMapper.insert(comment);
        commentService.updateCommentIsShow(comment.getId(), Comment.HIDE);
        comment = commentMapper.selectById(comment.getId());
        assertEquals(Comment.HIDE, comment.getIsShow());
        commentService.updateCommentIsShow(comment.getId(), Comment.SHOW);
        comment = commentMapper.selectById(comment.getId());
        assertEquals(Comment.SHOW, comment.getIsShow());
    }

    @Test
    void replyList() {
        // empty
        CommonPage<CommentReplay> page = commentService.replyList(1L, 20L, -1L);
        assertEquals(0, page.getData().size());

        // not empty
        List<CommentReplay> commentReplays = new ArrayList<>();
        for (int i = 0 ; i < 10; i++) {
            CommentReplay commentReplay = new CommentReplay();
            commentReplay.setCommentId(1L);
            commentReplayMapper.insert(commentReplay);
            commentReplays.add(commentReplay);
        }
        CommonPage<CommentReplay> page1 = commentService.replyList(1L, 10L, 1L);
        assertEquals(commentReplays.size(), page1.getData().size());
    }

    @Test
    void replyComment() {
        // illegal
        CommentReplyParam param = new CommentReplyParam();
        param.setCommentId(-1L);
        assertThrows(IllegalArgumentException.class, () -> commentService.replyComment(param));

        // legal
        Comment comment = new Comment();
        comment.setProductId(1L);
        commentMapper.insert(comment);
        CommentReplyParam param1 = new CommentReplyParam();
        param1.setCommentId(comment.getId());
        param1.setContent("content");
        param1.setMemberIcon("http://aha.jpg");
        param1.setMemberNickname("admin");
        commentService.replyComment(param1);
        Page<CommentReplay> page = new Page<>(1, 10);
        Page<CommentReplay> commentReplayPage = commentReplayMapper.selectPageByCommentId(page, comment.getId());
        assertEquals(1, commentReplayPage.getTotal());
    }
}