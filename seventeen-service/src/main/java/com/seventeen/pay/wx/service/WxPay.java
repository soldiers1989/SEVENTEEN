package com.seventeen.pay.wx.service;

import com.seventeen.pay.wx.bean.WxOrderBean;

import java.io.UnsupportedEncodingException;

public interface WxPay {

     String  creatOrder(WxOrderBean orderBean) throws UnsupportedEncodingException;
}
