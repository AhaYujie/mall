package online.ahayujie.mall.portal.pms.service;

import online.ahayujie.mall.common.api.CommonPage;
import online.ahayujie.mall.portal.pms.bean.dto.CommentDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayDTO;
import online.ahayujie.mall.portal.pms.bean.dto.CommentReplayParam;
import online.ahayujie.mall.portal.pms.bean.model.Comment;

/**
 * <p>
 * 商品评价表 服务类
 * </p>
 *
 * @author aha
 * @since 2020-10-19
 */
public interface CommentService {
    /**
     * 分页获取商品评价。
     * 评价的isShow =  #{@link Comment#SHOW}。
     *
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @param productId 商品id
     * @param category 0->全部评价，1->好评，2->中评，3->差评，4->有图
     * @param sort 0->按照点赞数从大到小排序，1->按照评价时间从新到旧排序
     * @return 商品评价
     * @throws IllegalArgumentException 参数不合法
     */
    CommonPage<CommentDTO> list(Long productId, Long pageNum, Long pageSize, Integer category, Integer sort) throws IllegalArgumentException;

    /**
     * 分页获取商品评价回复。
     * @param commentId 商品评价id
     * @param pageNum 页索引
     * @param pageSize 页大小
     * @return 商品评价回复
     */
    CommonPage<CommentReplayDTO> replyList(Long commentId, Long pageNum, Long pageSize);

    /**
     * 获取商品评价数量。
     * 评价的isShow =  #{@link Comment#SHOW}。
     *
     * @param productId 商品id
     * @param category 0->全部评价，1->好评，2->中评，3->差评，4->有图
     * @return 商品评价数量
     * @throws IllegalArgumentException 参数不合法
     */
    Long getCount(Long productId, Integer category) throws IllegalArgumentException;

    /**
     * 回复商品评价。
     * 操作成功则增加商品评价的回复数。
     * 操作成功发送消息到消息队列。
     *
     * @param param 参数
     * @throws IllegalArgumentException 商品评价不存在
     */
    void reply(CommentReplayParam param) throws IllegalArgumentException;
}
