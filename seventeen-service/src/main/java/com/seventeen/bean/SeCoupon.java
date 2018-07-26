package com.seventeen.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

public class SeCoupon {
    @Id
    private String id;

    private String name;

    private String status;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startTime;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endTime;

    private String sendType;

    private String price;

    private String maxPrice;

    private String remark;

    private String createTime;

    private String useType;

    private String createBy;

    @Transient
    private List<String> sendTypeArr;

    @Transient
    private List<String> priceTypes;

    public List<String> getPriceTypes() {
        return priceTypes;
    }

    public void setPriceTypes(List<String> priceTypes) {
        this.priceTypes = priceTypes;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }


    public List<String> getSendTypeArr() {
        return sendTypeArr;
    }

    public void setSendTypeArr(List<String> sendTypeArr) {
        this.sendTypeArr = sendTypeArr;
    }

    public String getId() {
        return id;
    }

    public SeCoupon setId(String id) {
        this.id = id == null ? null : id.trim();
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStatus() {
        return status;
    }

    public SeCoupon setStatus(String status) {
        this.status = status == null ? null : status.trim();
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType == null ? null : sendType.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice == null ? null : maxPrice.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }
}