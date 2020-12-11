package com.hlcy.yun.common.page;

public class PageInfo<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private T data;

    public PageInfo() {
    }

    public PageInfo(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageInfo(Long total, T data) {
        this.total = total;
        this.data = data;
    }

    public PageInfo(Integer pageNum, Integer pageSize, Long total, T data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
