package online.ahayujie.mall.portal.pms.bean.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aha
 * @since 2020/10/26
 */
@Data
public class EsProduct {
    private Long id;

    private Date updateTime;

    private Date createTime;

    private Long brandId;

    private Long productCategoryId;

    private String productSn;

    private String name;

    private String brandName;

    private String productCategoryName;

    private String pic;

    private String subTitle;

    private String description;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer sale;

    private Integer stock;

    private String unit;

    private String keywords;

    private Integer sort;

    private String serviceIds;

    private Integer promotionType;

    private Integer isPublish;

    private Integer isNew;

    private Integer isRecommend;

    private Integer isVerify;

    private Integer isPreview;

    private String params;

    private String specifications;
}
