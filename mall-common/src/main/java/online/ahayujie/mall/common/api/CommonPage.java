package online.ahayujie.mall.common.api;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 通用分页
 * @author aha
 * @date 2020/6/18
 */
public class CommonPage<T> {
    private Long pageNum;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private List<T> data;

    public CommonPage(Long pageNum, Long pageSize, Long totalPage, Long total, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.total = total;
        this.data = data;
    }

    public CommonPage(IPage<T> page) {
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
        this.totalPage = page.getPages();
        this.total = page.getTotal();
        this.data = page.getRecords();
    }

    @Override
    public String toString() {
        return "CommonPage{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", total=" + total +
                ", data=" + data +
                '}';
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
