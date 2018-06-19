package com.seventeen.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * @Author: csk
 * @Date: 2018/5/18 16:49
 */
@ApiModel("PageInfo(分页信息)")
public class PageInfo {
    @ApiModelProperty(value = "第几页，不传递则默认为第1页", example = "1")
    private int pageNum = 1;

    @ApiModelProperty(value = "每页有多少行，不传递则默认为10", example = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "总数", hidden = true)
    private long total;

    public PageInfo(PageInfo pageInfo) {
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
    }

    public PageInfo() {
    }

    public int getPageNum() {
        if (pageNum < 1) pageNum = 1;
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        if (pageSize < 1) pageSize = 10;
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

    @Override
    public String toString() {
        return "pageNum:" + pageNum + "_pageSize:" + pageSize;
    }

}
