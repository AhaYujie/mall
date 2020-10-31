package online.ahayujie.mall.search.bean.model;

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
 * 商品规格选项
 * </p>
 *
 * @author aha
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_specification_value")
@ApiModel(value="ProductSpecificationValue对象", description="商品规格选项")
public class ProductSpecificationValue extends Base {

    public static final String TEXT_TYPE_NAME = "textType";
    public static final String IMAGE_TEXT_TYPE_NAME = "imageTextType";

    public enum Type {
        /**
         * 纯文本：{"name" : "test"}
         */
        TEXT(0, TEXT_TYPE_NAME),

        /**
         * 图文：{"name" : "test", "image" : "http://test.jpg"}
         */
        IMAGE_TEXT(1, IMAGE_TEXT_TYPE_NAME)
        ;
        private final Integer value;
        private final String name;

        Type(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品规格id")
    private Long productSpecificationId;

    @ApiModelProperty(value = "选项值")
    private String value;

    @ApiModelProperty(value = "选项类型：0->纯文本，1->图文")
    private Integer type;

}
