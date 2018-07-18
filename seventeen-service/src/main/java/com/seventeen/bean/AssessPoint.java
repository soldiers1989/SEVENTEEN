package com.seventeen.bean;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/6/6 16:58
 */
public class AssessPoint {
    private String assessId;
    private String orderId;
    private String userName;
    private String shopName;
    private String isCheck;
    private String apNum;
    private String roomType;
    private String apName;
    private String totalPoint;
    private String orderTime;
    private String assessTime;
    private String contentId;

    private List<SeAssessPoint> seAssessPoints;
    private List<SeAssessContent> seAssessContents;
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getAssessId() {
        return assessId;
    }

    public void setAssessId(String assessId) {
        this.assessId = assessId;
    }

    public List<SeAssessPoint> getSeAssessPoints() {
        return seAssessPoints;
    }

    public void setSeAssessPoints(List<SeAssessPoint> seAssessPoints) {
        this.seAssessPoints = seAssessPoints;
    }

    public List<SeAssessContent> getSeAssessContents() {
        return seAssessContents;
    }

    public void setSeAssessContents(List<SeAssessContent> seAssessContents) {
        this.seAssessContents = seAssessContents;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getApNum() {
        return apNum;
    }

    public void setApNum(String apNum) {
        this.apNum = apNum;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(String assessTime) {
        this.assessTime = assessTime;
    }
}
