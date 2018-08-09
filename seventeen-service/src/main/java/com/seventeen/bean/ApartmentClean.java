package com.seventeen.bean;

public class ApartmentClean {


    private String apId;
    private String apNum;

    private String isCleaned;

    private String shopName;

    private String createTime;

    public String getApNum() {
        return apNum;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public void setApNum(String apNum) {
        this.apNum = apNum;
    }

    public String getIsCleaned() {
        return isCleaned;
    }

    public void setIsCleaned(String isCleaned) {
        this.isCleaned = isCleaned;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}