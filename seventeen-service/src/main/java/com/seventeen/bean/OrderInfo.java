package com.seventeen.bean;

public class OrderInfo {

    private String roomId;
    private String fee;
    private String startTime;
    private String endTime;
    private String tName;
    private String phone;
    private String planTime;
    private String couponId;
    private String price;
    private String roomNum;

    @Override
    public String toString() {
        return "OrderInfo{" +
                "roomId='" + roomId + '\'' +
                ", fee='" + fee + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", tName='" + tName + '\'' +
                ", phone='" + phone + '\'' +
                ", planTime='" + planTime + '\'' +
                ", couponId='" + couponId + '\'' +
                ", price='" + price + '\'' +
                ", roomNum='" + roomNum + '\'' +
                '}';
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }
}
