package online.ahayujie.mall.admin.oms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 公司收发货地址表 Mapper 接口
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Mapper
@Repository
public interface CompanyAddressMapper extends BaseMapper<CompanyAddress> {
    /**
     * 查询默认发货地址
     * @param sendDefault 默认发货地址状态值
     * @return 默认发货地址
     */
    CompanyAddress selectSendDefault(Integer sendDefault);

    /**
     * 查询默认收货地址
     * @param receiveDefault 默认收货地址状态值
     * @return 默认收货地址
     */
    CompanyAddress selectReceiveDefault(Integer receiveDefault);

    /**
     * 查询全部
     * @return 全部地址
     */
    List<CompanyAddress> selectAll();

    /**
     * 分页获取。
     * 按照默认发货地址状态(默认在前)，默认收货地址状态(默认在前)，启用状态(启用在前)，创建时间(从新到旧)排序。
     * @param page 分页参数
     * @return 公司地址
     */
    IPage<CompanyAddress> selectByPage(@Param("page") Page<CompanyAddress> page);
}
