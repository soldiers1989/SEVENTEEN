package com.seventeen.bean;

import javax.persistence.Id;
import java.util.Date;

public class SeUserVip {
    @Id
    private String id;

    private String level;

    private Date stratdate;

    private Integer status;

    private String discount;

    private String userid;

    private Integer roomNum;

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Date getStratdate() {
        return stratdate;
    }

    public void setStratdate(Date stratdate) {
        this.stratdate = stratdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount == null ? null : discount.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }
}