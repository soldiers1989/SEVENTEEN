package com.seventeen.bean;

import lombok.Data;

import java.util.List;

@Data
public class WxTotalAssess {

    private String totalAssess;

    private String totalContentSize;

    private List<AssessTarget> assessTargets;

    private List<AssessContent> assessContent;

    @Data
    public static class AssessTarget{
        private String target;
        private int point;
        private List<String> starArr;
    }

    @Data
    public static class AssessContent{
        private int totalPoint;
        private List<String> starArr;
        private String content;
        private String userName;
        private String userPhone;
        private String avatarUrl;
        private String roomName;
        private String createTime;


    }

}