package com.seventeen.bean;

import javax.persistence.Id;

public class SeAssess {
    @Id
    private String id;

    private String userId;

    private String orderId;

    private String apId;

    private String status;

    private String isCheck;

    private String targetTagId;

    private String baseTagId;

    private String assessPointId;

    private String contentId;

    private String createTime;

    private String createBy;

    public String getBaseTagId() {
        return baseTagId;
    }

    public void setBaseTagId(String baseTagId) {
        this.baseTagId = baseTagId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId == null ? null : apId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getTargetTagId() {
        return targetTagId;
    }

    public void setTargetTagId(String targetTagId) {
        this.targetTagId = targetTagId == null ? null : targetTagId.trim();
    }

    public String getAssessPointId() {
        return assessPointId;
    }

    public void setAssessPointId(String assessPointId) {
        this.assessPointId = assessPointId == null ? null : assessPointId.trim();
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId == null ? null : contentId.trim();
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