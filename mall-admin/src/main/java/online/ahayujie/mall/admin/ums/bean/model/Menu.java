package online.ahayujie.mall.admin.ums.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 后台菜单表
 * </p>
 *
 * @author aha
 * @since 2020-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ums_menu")
@ApiModel(value="Menu对象", description="后台菜单表")
public class Menu extends Base {

    private static final long serialVersionUID = 1L;

    public static final long NON_PARENT_ID = 0L;

    public enum VISIBILITY {
        /**
         * 显示
         */
        SHOW(0),

        /**
         * 隐藏
         */
        HIDDEN(1)
        ;

        private final Integer value;

        VISIBILITY(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    @ApiModelProperty(value = "是否删除，0->未删除，1->已删除")
    private Boolean isDeleted;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String title;

    @ApiModelProperty(value = "菜单级数")
    private Integer level;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    private String name;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端隐藏")
    private Integer hidden;


}
