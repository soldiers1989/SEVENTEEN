package com.seventeen.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserPoint {
    private String id;

    private String userName;

    private String userId;

    private String point;

    private String updateTime;

    private List<SeUserPointLog> seUserPointLogs;


}