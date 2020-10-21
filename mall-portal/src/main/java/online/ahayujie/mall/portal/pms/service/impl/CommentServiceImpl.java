package online.ahayujie.mall.portal.pms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.mms.bean.dto.MemberDTO;
import online.ahayujie.mall.portal.mms.service.MemberService;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayParam;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplyMsgDTO;
import online.ahayujie.mall.portal.pms.bean.model.Comment;
import online.ahayujie.mall.portal.pms.bean.model.CommentReplay;
import online.ahayujie.mall.portal.pms.mapper.CommentMapper;
import online.ahayujie.mall.portal.pms.mapper.CommentReplayMapper;
import online.ahayujie.mall.portal.pms.publisher.CommentPublisher;
import online.ahayujie.mall.portal.pms.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private MemberService memberService;
    private CommentPublisher commentPublisher;

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

    @Override
    public Long getCount(Long productId, Integer category) throws IllegalArgumentException {
        switch (category) {
            case 0:
                return commentMapper.selectCountByStarAndIsPic(productId, Comment.SHOW, null, null);
            case 1:
                return commentMapper.selectCountByStarAndIsPic(productId, Comment.SHOW, Comment.Star.GOOD.value(), null);
            case 2:
                return commentMapper.selectCountByStarAndIsPic(productId, Comment.SHOW, Comment.Star.NORMAL.value(), null);
            case 3:
                return commentMapper.selectCountByStarAndIsPic(productId, Comment.SHOW, Comment.Star.BAD.value(), null);
            case 4:
                return commentMapper.selectCountByStarAndIsPic(productId, Comment.SHOW, null, Comment.HAVE_PIC);
            default:
                throw new IllegalArgumentException("category参数不合法");
        }
    }

    @Override
    public void reply(CommentReplayParam param) throws IllegalArgumentException {
        Comment comment = commentMapper.selectReplayCount(param.getCommentId());
        if (comment == null) {
            throw new IllegalArgumentException("商品评价不存在");
        }
        comment.setReplayCount(comment.getReplayCount() + 1);
        commentMapper.updateById(comment);
        CommentReplay commentReplay = new CommentReplay();
        BeanUtils.copyProperties(param, commentReplay);
        MemberDTO memberDTO = memberService.getInfo();
        commentReplay.setMemberNickname(memberDTO.getNickname());
        commentReplay.setMemberIcon(memberDTO.getIcon());
        commentReplay.setType(CommentReplay.Type.MEMBER.getValue());
        commentReplay.setCreateTime(new Date());
        commentReplayMapper.insert(commentReplay);
        commentPublisher.publishCommentReplyMsg(new CommentReplyMsgDTO(param.getCommentId(), commentReplay.getId(), param.getContent()));
    }

    @Autowired
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Autowired
    public void setCommentPublisher(CommentPublisher commentPublisher) {
        this.commentPublisher = commentPublisher;
    }
}
