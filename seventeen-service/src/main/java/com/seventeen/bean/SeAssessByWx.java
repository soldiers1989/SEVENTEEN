package com.seventeen.bean;

import lombok.Data;

import java.util.List;

@Data
public class SeAssessByWx {

    private String orderId;

    private String content;

    private String liveSelected;

    private String targetSelected;

    private List<AssessPoint> point;

    @Data
    public static class AssessPoint{
        private String id;
        private List<String> star;
    }

}