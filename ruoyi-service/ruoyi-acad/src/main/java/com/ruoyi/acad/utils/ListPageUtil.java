package com.ruoyi.acad.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListPageUtil<T> {

    /**
     * 每页显示条数
     */
    private int pageSize;
    /**
     * 页码
     */
    private int pageNum;


    /**
     * 总页数
     */
    private int pageCount;

    /**
     * 原集合
     */
    private List<T> data;

    public ListPageUtil(List<T> data, int pageSize,int pageNum) {
        if (data == null || data.isEmpty()) {
            //throw new IllegalArgumentException("data must be not empty!");
            this.data = null;
            this.pageSize = 0;
            this.pageNum = 0;
            this.pageCount = 0;
            if(data.size()%pageSize!=0){
                this.pageCount++;
            }
        }else{
            this.data = data;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.pageCount = data.size()/pageSize;
            if(data.size()%pageSize!=0){
                this.pageCount++;
            }
        }
    }

    /**
     * 得到分页后的数据
     *
     * @return 分页后结果
     */
    public List<T> getPagedList() {
        if(data == null){
            return null;
        }else{
            int fromIndex = (pageNum - 1) * pageSize;
            if (fromIndex >= data.size()) {
                return Collections.emptyList();
            }

            int toIndex = pageNum * pageSize;
            if (toIndex >= data.size()) {
                toIndex = data.size();
            }
            return data.subList(fromIndex, toIndex);
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public int getPageCount() {
        return pageCount;
    }

    public static void main(String[] args) {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<Integer> list = Arrays.asList(array);

        ListPageUtil<Integer> pager = new ListPageUtil<Integer>(list, 5,2);

        System.out.println(pager.getPageCount());

        List<Integer> page1 = pager.getPagedList();
        System.out.println(page1);

    }
}
