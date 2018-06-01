package com.seventeen.bean;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/28 16:20
 */
public class SeApartmentDetail {
    private SeApartment seApartment;

    private List<SeApartmentImg> seApartmentImg;

    private List<String> good;

    public List<String> getGood() {
        return good;
    }

    public void setGood(List<String> good) {
        this.good = good;
    }

    public SeApartment getSeApartment() {
        return seApartment;
    }

    public void setSeApartment(SeApartment seApartment) {
        this.seApartment = seApartment;
    }

    public List<SeApartmentImg> getSeApartmentImg() {
        return seApartmentImg;
    }

    public void setSeApartmentImg(List<SeApartmentImg> seApartmentImg) {
        this.seApartmentImg = seApartmentImg;
    }
}
