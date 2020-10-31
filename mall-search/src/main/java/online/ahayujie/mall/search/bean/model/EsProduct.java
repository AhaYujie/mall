package online.ahayujie.mall.search.bean.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author aha
 * @since 2020/10/26
 */
@Data
@Document(indexName = "product", shards = 1, replicas = 0)
public class EsProduct {
    @Id
    private Long id;

    @Field(type = FieldType.Date)
    private Date updateTime;

    @Field(type = FieldType.Date)
    private Date createTime;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Long)
    private Long productCategoryId;

    @Field(type = FieldType.Keyword)
    private String productSn;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword)
    private String productCategoryName;

    private String pic;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String subTitle;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    @Field(type = FieldType.Integer)
    private Integer sale;

    private Integer stock;

    private String unit;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String keywords;

    @Field(type = FieldType.Integer)
    private Integer sort;

    private String serviceIds;

    private Integer promotionType;

    @Field(type = FieldType.Integer)
    private Integer isPublish;

    @Field(type = FieldType.Integer)
    private Integer isNew;

    @Field(type = FieldType.Integer)
    private Integer isRecommend;

    @Field(type = FieldType.Integer)
    private Integer isVerify;

    @Field(type = FieldType.Integer)
    private Integer isPreview;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String params;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String specifications;
}
