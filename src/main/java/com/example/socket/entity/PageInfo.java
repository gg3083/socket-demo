package com.example.socket.entity;

import lombok.Data;

import java.util.List;

/**
 * @author Gimi
 * @date 2019/8/16 10:25
 */
@Data
public class PageInfo<T>{

    private long pageNo;
    private long pageSize;
    private long totals;
    private List<T> data;

    public PageInfo(long pageNo, long pageSize, long totals, List<T> data){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totals = totals;
        this.data = data;
    }
    public PageInfo(com.github.pagehelper.PageInfo pageInfo){
            this.pageNo = pageInfo.getPageNum();
            this.pageSize = pageInfo.getPageSize();
            this.totals = pageInfo.getTotal();
            this.data = pageInfo.getList();
    }

}
