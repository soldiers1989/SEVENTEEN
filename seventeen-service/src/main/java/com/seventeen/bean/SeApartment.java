package com.seventeen.bean;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;


public class SeApartment {

    @Id
    private String id;

    private String apNum;

    private String shopId;

    private String name;

    private String type;

    private String title;

    private String status;

    private String bed;

    private String floor;

    private String area;

    private String structure;

    private String price;

    private String createTime;

    private String createBy;

    private String remark;

    private String roomType;

    private String LockId;

    private String LockPwdId;

    @Transient
    private ArrayList<String> good;


    public String getLockId() {
        return LockId;
    }

    public void setLockId(String lockId) {
        LockId = lockId;
    }

    public String getLockPwdId() {
        return LockPwdId;
    }

    public void setLockPwdId(String lockPwdId) {
        LockPwdId = lockPwdId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public ArrayList<String> getGood() {
        return good;
    }

    public void setGood(ArrayList<String> good) {
        this.good = good;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getApNum() {
        return apNum;
    }

    public void setApNum(String apNum) {
        this.apNum = apNum == null ? null : apNum.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure == null ? null : structure.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
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