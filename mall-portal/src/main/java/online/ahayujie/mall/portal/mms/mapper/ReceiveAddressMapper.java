package online.ahayujie.mall.portal.mms.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.portal.mms.bean.dto.ReceiveAddressDTO;
import online.ahayujie.mall.portal.mms.bean.model.ReceiveAddress;
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
 * @since 2020-10-10
 */
@Mapper
@Repository
public interface ReceiveAddressMapper extends BaseMapper<ReceiveAddress> {
    /**
     * 根据会员id分页查询。
     * 根据isDefault从大到小排序
     * @param page 分页参数
     * @param memberId 会员id
     * @return 收货地址
     */
    Page<ReceiveAddressDTO> selectPageByMemberId(@Param("page") Page<ReceiveAddressDTO> page,
                                                 @Param("memberId") Long memberId);

    /**
     * 根据会员id查询默认收货地址
     * @param memberId 会员id
     * @param defaultValue 默认值
     * @return 默认收货地址
     */
    ReceiveAddress selectDefaultByMemberId(@Param("memberId") Long memberId, @Param("defaultValue") Integer defaultValue);

    /**
     * 根据id和会员id查询收货地址
     * @param id 收货地址id
     * @param memberId 会员id
     * @return 收货地址
     */
    ReceiveAddress selectByIdAndMemberId(@Param("id") Long id, @Param("memberId") Long memberId);
}
