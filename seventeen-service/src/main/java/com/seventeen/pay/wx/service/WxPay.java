package com.seventeen.pay.wx.service;

import java.io.UnsupportedEncodingException;

public interface WxPay {

     String  creatOrder() throws UnsupportedEncodingException;

     String cancelOrder(String orderId) throws Exception;
}
