package com.zhangbin.yun.yunrights.modules.common.model.vo;

public class PageInfo<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private T data;

    public PageInfo(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageInfo(long total, T data) {
        this.total = total;
        this.data = data;
    }

    public PageInfo(int pageNum, int pageSize, long total, T data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
