package online.ahayujie.mall.portal.pms.service;

import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.model.Comment;
import online.ahayujie.mall.portal.pms.bean.model.CommentReplay;
import online.ahayujie.mall.portal.pms.mapper.CommentMapper;
import online.ahayujie.mall.portal.pms.mapper.CommentReplayMapper;
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
class CommentServiceTest extends TestBase {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentReplayMapper commentReplayMapper;

    @Test
    void list() {
        // not exist
        CommonPage<CommentDTO> result = commentService.list(-1L, 1L, 20L, 0, 0);
        assertEquals(0, result.getData().size());

        Long productId = 1L;
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Comment comment = new Comment();
            comment.setProductId(productId);
            comment.setStar(i % 6);
            comment.setLikeCount(i);
            comment.setIsPic(i % 2);
            commentMapper.insert(comment);
            comments.add(comment);
        }
        // 全部评价
        CommonPage<CommentDTO> result1 = commentService.list(productId, 1L, 20L, 0, 0);
        assertEquals(comments.size(), result1.getData().size());
        Integer likeCount = Integer.MAX_VALUE;
        for (CommentDTO commentDTO : result1.getData()) {
            assertTrue(likeCount > commentDTO.getLikeCount());
            likeCount = commentDTO.getLikeCount();
        }
        // 好评
        CommonPage<CommentDTO> result2 = commentService.list(productId, 1L, 20L, 1, 0);
        for (CommentDTO commentDTO : result2.getData()) {
            assertEquals(Comment.Star.GOOD.value(), commentDTO.getStar());
        }
        // 中评
        CommonPage<CommentDTO> result3 = commentService.list(productId, 1L, 20L, 2, 0);
        for (CommentDTO commentDTO : result3.getData()) {
            assertEquals(Comment.Star.NORMAL.value(), commentDTO.getStar());
        }
        // 差评
        CommonPage<CommentDTO> result4 = commentService.list(productId, 1L, 20L, 3, 1);
        for (CommentDTO commentDTO : result4.getData()) {
            assertEquals(Comment.Star.BAD.value(), commentDTO.getStar());
        }
    }

    @Test
    void replyList() {
        // not exist
        CommonPage<CommentReplayDTO> result = commentService.replyList(-1L, 1L, 20L);
        assertEquals(0, result.getData().size());

        // exist
        Comment comment = new Comment();
        comment.setProductId(1L);
        commentMapper.insert(comment);
        for (int i = 0; i < 10; i++) {
            CommentReplay commentReplay = new CommentReplay();
            commentReplay.setCommentId(comment.getId());
            commentReplayMapper.insert(commentReplay);
        }
        CommonPage<CommentReplayDTO> result1 = commentService.replyList(comment.getId(), 1L, 10L);
        assertEquals(10, result1.getData().size());
    }
}