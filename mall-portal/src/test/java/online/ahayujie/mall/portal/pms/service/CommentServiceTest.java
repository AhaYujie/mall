package online.ahayujie.mall.portal.pms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.TestBase;
import online.ahayujie.mall.portal.mms.mapper.MemberMapper;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayParam;
import online.ahayujie.mall.portal.pms.bean.model.Comment;
import online.ahayujie.mall.portal.pms.bean.model.CommentReplay;
import online.ahayujie.mall.portal.pms.bean.model.Product;
import online.ahayujie.mall.portal.pms.mapper.CommentMapper;
import online.ahayujie.mall.portal.pms.mapper.CommentReplayMapper;
import online.ahayujie.mall.portal.pms.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;
    @Value("${jwt.header}")
    private String JWT_HEADER;
    @Value("${jwt.header-prefix}")
    private String JWT_HEADER_PREFIX;

    @BeforeEach
    void setUp() {
        initMember(passwordEncoder, memberMapper, memberService, JWT_HEADER, JWT_HEADER_PREFIX);
    }

    @Test
    void list() {
        // not exist
        CommonPage<CommentDTO> result = commentService.list(-1L, 1L, 20L, 0, 0);
        assertEquals(0, result.getData().size());

        Product product = new Product();
        product.setName(getRandomString(10));
        productMapper.insert(product);
        Long productId = product.getId();
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

    @Test
    void getCount() {
        // 0
        Long count = commentService.getCount(-1L, 0);
        assertEquals(0L, count);

        // exist
        Product product = new Product();
        product.setName(getRandomString(10));
        productMapper.insert(product);
        Long productId = product.getId();
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
        // all
        Long count1 = commentService.getCount(productId, 0);
        assertEquals(comments.size(), count1);
        // good
        Long count2 = commentService.getCount(productId, 1);
        Long result = 0L;
        for (Comment comment : comments) {
            if (Comment.Star.GOOD.value().equals(comment.getStar()))
                result++;
        }
        assertEquals(result, count2);
        // normal
        Long count3 = commentService.getCount(productId, 2);
        Long result1 = 0L;
        for (Comment comment : comments) {
            if (Comment.Star.NORMAL.value().equals(comment.getStar()))
                result1++;
        }
        assertEquals(result1, count3);
        // bad
        Long count4 = commentService.getCount(productId, 3);
        Long result2 = 0L;
        for (Comment comment : comments) {
            if (Comment.Star.BAD.value().equals(comment.getStar()))
                result2++;
        }
        assertEquals(result2, count4);
        // have pic
        Long count5 = commentService.getCount(productId, 4);
        Long result3 = 0L;
        for (Comment comment : comments) {
            if (Comment.HAVE_PIC == comment.getIsPic())
                result3++;
        }
        assertEquals(result3, count5);
    }

    @Test
    void reply() {
        // legal
        Comment comment = new Comment();
        comment.setProductId(1L);
        commentMapper.insert(comment);
        CommentReplayParam param = new CommentReplayParam();
        param.setCommentId(comment.getId());
        param.setContent(getRandomString(50));
        List<CommentReplay> oldList = commentReplayMapper.selectList(Wrappers.emptyWrapper());
        commentService.reply(param);
        List<CommentReplay> newList = commentReplayMapper.selectList(Wrappers.emptyWrapper());
        CommentReplay create = null;
        for (CommentReplay tmp : newList) {
            if (!oldList.contains(tmp)) {
                create = tmp;
                break;
            }
        }
        assertNotNull(create);
        assertEquals(param.getCommentId(), create.getCommentId());
        assertEquals(param.getContent(), create.getContent());
        assertEquals(member.getNickname(), create.getMemberNickname());
        assertEquals(member.getIcon(), create.getMemberIcon());
        comment = commentMapper.selectById(comment.getId());
        assertEquals(1, comment.getReplayCount());

        // illegal
        CommentReplayParam param1 = new CommentReplayParam();
        param1.setCommentId(-1L);
        assertThrows(IllegalArgumentException.class, () -> commentService.reply(param1));
    }
}