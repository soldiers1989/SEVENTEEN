package com.seventeen.bean;

import java.util.List;

public class AddLiver {
    private List<SeOrderLiver> addLiver;

    private String orderid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public List<SeOrderLiver> getAddLiver() {
        return addLiver;
    }

    public void setAddLiver(List<SeOrderLiver> addLiver) {
        this.addLiver = addLiver;
    }
}