package online.ahayujie.mall.admin.pms.service.impl;

import online.ahayujie.mall.admin.pms.bean.model.ProductVerifyLog;
import online.ahayujie.mall.admin.pms.mapper.ProductVerifyLogMapper;
import online.ahayujie.mall.admin.pms.service.ProductVerifyLogService;
import online.ahayujie.mall.admin.ums.bean.model.Admin;
import online.ahayujie.mall.admin.ums.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 商品审核记录 服务实现类
 * </p>
 *
 * @author aha
 * @since 2020-08-04
 */
@Service
public class ProductVerifyLogServiceImpl implements ProductVerifyLogService {
    private AdminService adminService;

    private final ProductVerifyLogMapper productVerifyLogMapper;

    public ProductVerifyLogServiceImpl(ProductVerifyLogMapper productVerifyLogMapper) {
        this.productVerifyLogMapper = productVerifyLogMapper;
    }

    @Override
    public void saveLog(Long productId, String note, Integer isVerify) {
        ProductVerifyLog productVerifyLog = new ProductVerifyLog();
        productVerifyLog.setProductId(productId);
        productVerifyLog.setNote(note);
        productVerifyLog.setIsVerify(isVerify);
        productVerifyLog.setCreateTime(new Date());
        Admin admin = adminService.getAdminFromToken();
        if (admin != null) {
            productVerifyLog.setAdminId(admin.getId());
            productVerifyLog.setUsername(admin.getUsername());
        }
        productVerifyLogMapper.insert(productVerifyLog);
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }
}
