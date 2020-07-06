package online.ahayujie.mall.admin.ums.event;

import online.ahayujie.mall.admin.ums.bean.model.Admin;
import org.springframework.context.ApplicationEvent;

/**
 * 删除后台用户事件
 * source保存被删除的后台用户信息
 * @author aha
 * @date 2020/7/6
 */
public class DeleteAdminEvent extends ApplicationEvent {
    public DeleteAdminEvent(Admin admin) {
        super(admin);
    }

    @Override
    public Admin getSource() {
        return (Admin) super.getSource();
    }
}
