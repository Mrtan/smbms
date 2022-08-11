package com.kuang.utils;

import org.junit.Test;

public class PageSupport {
    private int totalPageCount = 0;
    private int totalCount = 0;
    private int currentPageNo = 1;
    private int pageSize = Constants.PAGE_SIZE;

    public PageSupport() {
    }

    public PageSupport(int totalCount, int currentPageNo) {
        this.totalCount = totalCount;
        this.currentPageNo = currentPageNo;
    }

    public int getTotalPageCount() {
        return (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1);
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
