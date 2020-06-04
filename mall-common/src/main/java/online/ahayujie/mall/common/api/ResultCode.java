package online.ahayujie.mall.common.api;

/**
 * 响应结果
 * @author aha
 * @date 2020/5/21
 */
public interface ResultCode {

    /**
     * 获取响应码
     * @return 响应码
     */
    Integer getCode();

    /**
     * 获取响应信息
     * @return 响应信息
     */
    String getMessage();

}
