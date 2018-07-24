package com.seventeen.bean;

import lombok.Data;

import java.util.List;

@Data
public class ApartmentPriceRoom {

    private String roomTypeId;

    private String name;

    private List<Domain> domains;

    @Data
    public static class Domain{
        private String priceTypeId;
        private String price;
        private String type;
        private List<String> time;
    }

}