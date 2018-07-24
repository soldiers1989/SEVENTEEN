package com.seventeen.bean;

import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "se_assess_point")
public class SeAssessPoint {

    private String id;

    private String tagId;

    @Transient
    private String name;

    private String point;

    private String pointArr;

    private String createTime;

    private String createBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointArr() {
        return pointArr;
    }


    public void setPointArr(String pointArr) {
        this.pointArr = pointArr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point == null ? null : point.trim();
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