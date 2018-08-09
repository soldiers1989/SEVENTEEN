package com.seventeen.bean;

import javax.persistence.Id;


public class SeOrderPay  {
    @Id
    private String id;
    @Id
    private String seOrderId;
    
    private String creatTime;

    private String payTime;

    private String outPayTime;

    private Integer status;

    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSeOrderId() {
        return seOrderId;
    }

    public void setSeOrderId(String seOrderId) {
        this.seOrderId = seOrderId == null ? null : seOrderId.trim();
    }
    
    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOutPayTime() {
        return outPayTime;
    }

    public void setOutPayTime(String outPayTime) {
        this.outPayTime = outPayTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}