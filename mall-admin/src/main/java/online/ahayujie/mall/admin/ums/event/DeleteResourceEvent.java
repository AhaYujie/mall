package online.ahayujie.mall.admin.ums.event;

import online.ahayujie.mall.admin.ums.bean.model.Resource;
import org.springframework.context.ApplicationEvent;

/**
 * 删除资源事件
 * source是被删除的资源数据
 * @author aha
 * @date 2020/7/6
 */
public class DeleteResourceEvent extends ApplicationEvent {
    public DeleteResourceEvent(Resource resource) {
        super(resource);
    }

    @Override
    public Resource getSource() {
        return (Resource) super.getSource();
    }
}
