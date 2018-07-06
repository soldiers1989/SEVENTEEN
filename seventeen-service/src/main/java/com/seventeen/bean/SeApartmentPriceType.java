package com.seventeen.bean;

import javax.persistence.Table;

@Table(name = "se_apartment_price_type")
public class SeApartmentPriceType  {

    private String apTypeId;

    private String tagId;

    private String price;

    private String startTime;

    private String endTime;

    private String status;

    private String createTime;

    private String createBy;

    public String getApTypeId() {
        return apTypeId;
    }

    public void setApTypeId(String apTypeId) {
        this.apTypeId = apTypeId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}