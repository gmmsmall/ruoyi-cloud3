package com.ruoyi.fabric.utils;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
    private static final long serialVersionUID = 5760097915453738435L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 每页显示个数
     */
    private int pageSize;
    /**
     * 当前页数
     */
    private int currentPage;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 结果列表
     */
    private List<T> rows;

    public Page(){
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }
    public Page(int currentPage,int pageSize){
        this.currentPage=currentPage<=0?1:currentPage;
        this.pageSize=pageSize<=0?1:pageSize;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        //设置了totalCount就可以计算出总totalPage
        this.totalCount = totalCount;
        int countRecords = this.getTotalCount();
        int totalPages = countRecords % pageSize == 0 ? countRecords / pageSize : (countRecords / pageSize + 1);
        setTotalPage(totalPages);
    }

    /**
     * 设置结果 及总页数
     * @param rows 分页之后查询到的结果
     */
    public void build(List<T> rows) {
        this.setRows(rows);
        int count =  this.getTotalCount();
				/*
            int divisor = count / this.getPageSize();
            int remainder = count % this.getPageSize();
            //设置总页数， Trash code, confusing.
            this.setTotalPage(remainder == 0 ? (divisor == 0 ? 1 : divisor) : divisor + 1);
				*/
        //已在setTotalCount中进行
			  /*
int countRecords = this.getTotalCount();
           int totalPages = countRecords % pageSize == 0 ? countRecords / pageSize : (countRecords / pageSize + 1);
			  setTotalPage(totalPages);
          */
    }
    public List<T> getRows() {
        return rows;
    }
    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
