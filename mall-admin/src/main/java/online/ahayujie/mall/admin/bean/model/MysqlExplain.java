package online.ahayujie.mall.admin.bean.model;

import lombok.Data;

/**
 * @author aha
 * @since 2020/9/29
 */
@Data
public class MysqlExplain {
    private Long id;
    private String selectType;
    private String table;
    private String partitions;
    private String type;
    private String possibleKeys;
    private String key;
    private String keyLen;
    private String ref;
    private Long rows;
    private String filtered;
    private String extra;
}
