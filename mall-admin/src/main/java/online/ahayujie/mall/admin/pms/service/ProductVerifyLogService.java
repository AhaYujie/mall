package online.ahayujie.mall.admin.pms.service;

/**
 * <p>
 * 商品审核记录 服务类
 * </p>
 *
 * @author aha
 * @since 2020-08-04
 */
public interface ProductVerifyLogService {
    /**
     * 保存审核商品记录
     * @param productId 商品id
     * @param note 备注
     * @param isVerify 审核状态
     */
    void saveLog(Long productId, String note, Integer isVerify);
}
