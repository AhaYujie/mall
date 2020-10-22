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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
        comment = commentMapper.selectById(comment.getId());
        assertEquals(1, comment.getReplayCount());
    }

    void test() {
        Random random = new Random();
        List<Long> productIds = new ArrayList<>();
        for (long i = 10000; i < 452510; i++) {
            productIds.add(i);
        }
        List<Long> memberIds = new ArrayList<>();
        for (long i = 1; i < 10000; i++) {
            memberIds.add(i);
        }
        List<String> specifications = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int j = 0; j < random.nextInt(4) + 1; j++) {
                String s = String.format("{\"key\":\"%s\",\"value\":\"%s\"}", getRandomString(random.nextInt(5)+2),
                        getRandomString(random.nextInt(5)+2));
                sb.append(s).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            specifications.add(sb.toString());
        }
        for (int i = 0; i < 50000; i++) {
            threadPoolTaskExecutor.execute(() -> {
                List<Comment> comments = new ArrayList<>();
                for (int j = 0; j < 20; j++) {
                    comments.add(createComment(random, productIds, memberIds, specifications));
                }
                commentMapper.insertList(comments);
            });
        }
        for (int i = 0; i < 5000; i++) {
            log.info("主线程：{}", i);
            List<Comment> comments = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                comments.add(createComment(random, productIds, memberIds, specifications));
            }
            commentMapper.insertList(comments);
        }
    }

    void test1() {
        Random random = new Random();
        List<Long> commentIds = new ArrayList<>();
        for (long i = 1L; i < 844308L; i++) {
            commentIds.add(i);
        }
        for (int i = 0; i < 100000; i++) {
            threadPoolTaskExecutor.execute(() -> {
                List<CommentReplay> commentReplays = new ArrayList<>();
                for (int j = 0; j < 20; j++) {
                    commentReplays.add(createCommentReplay(random, commentIds));
                }
                commentReplayMapper.insertList(commentReplays);
            });
        }
        for (int i = 0; i < 10000; i++) {
            log.info("主线程：{}", i);
            List<CommentReplay> commentReplays = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                commentReplays.add(createCommentReplay(random, commentIds));
            }
            commentReplayMapper.insertList(commentReplays);
        }
    }

    private Comment createComment(Random random, List<Long> productIds, List<Long> memberIds, List<String> specifications) {
        Comment comment = new Comment();
        comment.setCreateTime(new Date());
        comment.setProductId(productIds.get(random.nextInt(productIds.size())));
        comment.setMemberId(memberIds.get(random.nextInt(memberIds.size())));
        comment.setMemberNickname(getRandomString(random.nextInt(30) + 5));
        comment.setMemberIcon("http://" + getRandomString(100) + ".jpg");
        comment.setProductName(getRandomString(30) + 10);
        comment.setSpecification(specifications.get(random.nextInt(specifications.size())));
        comment.setContent(getRandomString(800) + 10);
        comment.setPics("http://" + getRandomString(100) + ".jpg");
        comment.setStar(random.nextInt(6));
        comment.setLikeCount(random.nextInt(100000));
        comment.setReadCount(random.nextInt(100000));
        comment.setReplayCount(random.nextInt(100));
        comment.setIsShow(random.nextInt(2));
        return comment;
    }

    private CommentReplay createCommentReplay(Random random, List<Long> commentIds) {
        CommentReplay commentReplay = new CommentReplay();
        commentReplay.setCreateTime(new Date());
        commentReplay.setCommentId(commentIds.get(random.nextInt(commentIds.size())));
        commentReplay.setMemberNickname(getRandomString(random.nextInt(10) + 5));
        commentReplay.setMemberIcon("http://" + getRandomString(random.nextInt(100) + 50) + ".jpg");
        commentReplay.setContent(getRandomString(random.nextInt(500) + 10));
        commentReplay.setType(random.nextInt(2));
        return commentReplay;
    }

    private static String getRandomString(int length) {
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}