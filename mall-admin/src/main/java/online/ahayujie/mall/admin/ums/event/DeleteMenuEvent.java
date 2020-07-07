package online.ahayujie.mall.admin.ums.event;

import online.ahayujie.mall.admin.ums.bean.model.Menu;
import org.springframework.context.ApplicationEvent;

/**
 * 删除菜单事件
 * source保存被删除的菜单数据
 * @author aha
 * @date 2020/7/6
 */
public class DeleteMenuEvent extends ApplicationEvent {
    public DeleteMenuEvent(Menu menu) {
        super(menu);
    }

    @Override
    public Menu getSource() {
        return (Menu) super.getSource();
    }
}
