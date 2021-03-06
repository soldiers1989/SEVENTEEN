package com.seventeen.pay.wx.util;

import com.github.wxpay.sdk.WXPayConfig;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyConfig implements WXPayConfig

{
    private byte[] certData;

    public MyConfig() throws Exception {
//        String certPath = "C:/Users/dell/Desktop/apiclient_cert.p12";
        String certPath = "/usr/zs/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    public String getAppID() {
        return "wxe6f9450a01336cb9";
    }

    @Override
    public String getMchID() {
        return "1509646201";
    }

    @Override
    public String getKey() {
        return "1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy";
    }


//    @Override
//    public IWXPayDomain getWXPayDomain() {
//
//        return WXPayDomainImpl.instance();
//    }
}
