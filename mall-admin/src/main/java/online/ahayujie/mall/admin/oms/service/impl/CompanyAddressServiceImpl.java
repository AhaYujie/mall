package online.ahayujie.mall.admin.oms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.ahayujie.mall.admin.oms.bean.dto.CreateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.dto.UpdateCompanyAddressParam;
import online.ahayujie.mall.admin.oms.bean.model.CompanyAddress;
import online.ahayujie.mall.admin.oms.exception.IllegalCompanyAddressException;
import online.ahayujie.mall.admin.oms.mapper.CompanyAddressMapper;
import online.ahayujie.mall.admin.oms.service.CompanyAddressService;
import online.ahayujie.mall.common.api.CommonPage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 公司收发货地址表 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-08
 */
@Service
public class CompanyAddressServiceImpl implements CompanyAddressService {
    private final CompanyAddressMapper companyAddressMapper;

    public CompanyAddressServiceImpl(CompanyAddressMapper companyAddressMapper) {
        this.companyAddressMapper = companyAddressMapper;
    }

    @Override
    public void create(CreateCompanyAddressParam param) throws IllegalCompanyAddressException {
        CompanyAddress companyAddress = new CompanyAddress();
        BeanUtils.copyProperties(param, companyAddress);
        validate(companyAddress);
        validateDefault(companyAddress.getIsSendDefault(), companyAddress.getIsReceiveDefault());
        companyAddress.setCreateTime(new Date());
        companyAddressMapper.insert(companyAddress);
    }

    @Override
    public void update(Long id, UpdateCompanyAddressParam param) throws IllegalCompanyAddressException {
        CompanyAddress old = companyAddressMapper.selectById(id);
        if (old == null) {
            throw new IllegalCompanyAddressException("公司地址不存在");
        }
        CompanyAddress companyAddress = new CompanyAddress();
        BeanUtils.copyProperties(param, companyAddress);
        validate(companyAddress);
        validateDefault(companyAddress.getIsSendDefault(), companyAddress.getIsReceiveDefault());
        companyAddress.setId(id);
        companyAddress.setUpdateTime(new Date());
        companyAddressMapper.updateById(companyAddress);
    }

    @Override
    public void delete(Long id) {
        companyAddressMapper.deleteById(id);
    }

    @Override
    public CommonPage<CompanyAddress> list(Integer pageNum, Integer pageSize) {
        Page<CompanyAddress> page = new Page<>(pageNum, pageSize);
        IPage<CompanyAddress> companyAddressPage = companyAddressMapper.selectByPage(page);
        return new CommonPage<>(companyAddressPage);
    }

    /**
     * 检查公司地址信息合法性。
     * 若某一字段为null，则忽略该字段不检查。
     * @param companyAddress 公司地址信息
     * @throws IllegalCompanyAddressException 公司地址信息不合法
     */
    private void validate(CompanyAddress companyAddress) throws IllegalCompanyAddressException {
        Integer isSendDefault = companyAddress.getIsSendDefault();
        if (isSendDefault != null) {
            if (!isSendDefault.equals(CompanyAddress.NOT_SEND_DEFAULT) &&
                !isSendDefault.equals(CompanyAddress.SEND_DEFAULT)) {
                throw new IllegalCompanyAddressException("默认发货地址状态不合法：" + isSendDefault);
            }
        }
        Integer isReceiveDefault = companyAddress.getIsReceiveDefault();
        if (isReceiveDefault != null) {
            if (!isReceiveDefault.equals(CompanyAddress.NOT_RECEIVE_DEFAULT) &&
                    !isReceiveDefault.equals(CompanyAddress.RECEIVE_DEFAULT)) {
                throw new IllegalCompanyAddressException("默认收货地址状态不合法：" + isReceiveDefault);
            }
        }
        Integer status = companyAddress.getStatus();
        if (status != null) {
            if (!status.equals(CompanyAddress.NOT_ACTIVE_STATUS) &&
                    !status.equals(CompanyAddress.ACTIVE_STATUS)) {
                throw new IllegalCompanyAddressException("启用状态不合法：" + status);
            }
        }
    }

    /**
     * 检查默认收发货地址合法性。
     * 若 {@code isSendDefault} 不为null且与 {@link CompanyAddress#SEND_DEFAULT} 相等
     * 且已存在默认发货地址则抛出异常。
     * 若 {@code isReceiveDefault} 不为null且与 {@link CompanyAddress#RECEIVE_DEFAULT} 相等
     * 且已存在默认收货地址则抛出异常。
     * @param isSendDefault 是否为默认发货地址
     * @param isReceiveDefault 是否为默认收货地址
     * @throws IllegalCompanyAddressException 默认收发货地址已存在
     */
    private void validateDefault(Integer isSendDefault, Integer isReceiveDefault) throws IllegalCompanyAddressException {
        if (isSendDefault != null && isSendDefault.equals(CompanyAddress.SEND_DEFAULT) &&
                isSendDefaultExist()) {
            throw new IllegalCompanyAddressException("默认发货地址已存在");
        }
        if (isReceiveDefault != null && isReceiveDefault.equals(CompanyAddress.RECEIVE_DEFAULT) &&
                isReceiveDefaultExist()) {
            throw new IllegalCompanyAddressException("默认收货地址已存在");
        }
    }

    /**
     * 判断默认发货地址是否存在
     * @return 存在则true，否则false
     */
    private boolean isSendDefaultExist() {
        return companyAddressMapper.selectSendDefault(CompanyAddress.SEND_DEFAULT) != null;
    }

    /**
     * 判断默认收货地址是否存在
     * @return 存在则true，否则false
     */
    private boolean isReceiveDefaultExist() {
        return companyAddressMapper.selectReceiveDefault(CompanyAddress.RECEIVE_DEFAULT) != null;
    }
}
