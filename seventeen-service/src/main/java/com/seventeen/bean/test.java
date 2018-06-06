package com.seventeen.bean;

import javax.persistence.Id;

public class test implements Runnable {

    @Id
    private String id;

    private String type;

    private String name;

    private String value;


    @Override
    public void run() {

    }
}