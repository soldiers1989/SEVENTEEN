package com.seventeen.bean;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/6/6 16:58
 */
public class OrderCenter {
    private String id;
    private String userName;
    private String apId;
    private String apName;
    private String status;
    private String couponName;
    private String liveTime;
    private String orderTime;
    private String price;

    private List<OrderLiver> livers;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public List<OrderLiver> getLivers() {
        return livers;
    }

    public void setLivers(List<OrderLiver> livers) {
        this.livers = livers;
    }
}
