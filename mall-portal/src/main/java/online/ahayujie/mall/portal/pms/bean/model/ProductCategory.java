package online.ahayujie.mall.portal.pms.bean.model;

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
 * 商品分类
 * </p>
 *
 * @author aha
 * @since 2020-10-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_category")
@ApiModel(value="ProductCategory对象", description="商品分类")
public class ProductCategory extends Base {

    public static final long NON_PARENT_ID = 0L;

    public static final int FIRST_LEVEL = 0;

    public static final int SECOND_LEVEL = 1;

    public enum ShowStatus {
        /**
         * 不显示在导航栏
         */
        NOT_SHOW(0),

        /**
         * 显示在导航栏
         */
        SHOW(1)
        ;
        private final Integer value;

        ShowStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上级分类的编号：0表示一级分类")
    private Long parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "分类级别：0->1级；1->2级")
    private Integer level;

    @ApiModelProperty(value = "商品数量")
    private Integer productCount;

    @ApiModelProperty(value = "商品单位")
    private String productUnit;

    @ApiModelProperty(value = "是否显示在导航栏：0->不显示；1->显示")
    private Integer isNav;

    @ApiModelProperty(value = "是否在移动端显示：0->不显示；1->显示")
    private Integer isShow;

    @ApiModelProperty(value = "排序，从大到小排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "描述")
    private String description;

}
