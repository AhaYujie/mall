package online.ahayujie.mall.admin.pms.bean.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.ToString;
import online.ahayujie.mall.common.bean.model.Base;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品参数
 * </p>
 *
 * @author aha
 * @since 2020-07-14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_product_param")
@ApiModel(value="ProductParam对象", description="商品参数")
public class ProductParam extends Base {
    public static final String TEXT_TYPE = "productTextTypeParam";
    public static final String IMAGE_TEXT_TYPE = "productImageTextTypeParam";

    public enum Type {
        /**
         * 纯文本
         */
        TEXT(0, TEXT_TYPE),

        /**
         * 图文
         */
        IMAGE_TEXT(1, IMAGE_TEXT_TYPE)
        ;
        private final Integer value;
        private final String name;

        Type(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Integer getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long productId;

    @ApiModelProperty(value = "参数名称")
    private String name;

    @ApiModelProperty(value = "参数值")
    private String value;

    @ApiModelProperty(value = "参数类型：0->纯文本，1->图文")
    private Integer type;


}
