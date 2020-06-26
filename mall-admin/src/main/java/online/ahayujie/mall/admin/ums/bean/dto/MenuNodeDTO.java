package online.ahayujie.mall.admin.ums.bean.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import online.ahayujie.mall.admin.ums.bean.model.Menu;

import java.util.List;

/**
 * @author aha
 * @date 2020/6/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuNodeDTO extends Menu {
    private List<MenuNodeDTO> children;
}
