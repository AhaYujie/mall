package online.ahayujie.mall.admin.mms.mapper;

import online.ahayujie.mall.admin.mms.bean.model.ReceiveAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员收货地址表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-22
 */
@Mapper
@Repository
public interface ReceiveAddressMapper extends BaseMapper<ReceiveAddress> {
    /**
     * 获取会员的默认收货地址
     * @param memberId 会员id
     * @param defaultValue 默认值
     * @return 会员的默认收货地址
     */
    ReceiveAddress selectDefault(@Param("memberId") Long memberId, @Param("defaultValue") Integer defaultValue);

    /**
     * 根据会员id删除
     * @param memberId 会员id
     * @return 删除数量
     */
    Integer deleteByMemberId(Long memberId);
}
