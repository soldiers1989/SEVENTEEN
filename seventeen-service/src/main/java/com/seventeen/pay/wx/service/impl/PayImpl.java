package com.seventeen.pay.wx.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.SeOrder;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.pay.wx.service.WxPay;
import com.seventeen.pay.wx.util.MyConfig;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayImpl implements WxPay {

    @Autowired
    SeOrderMapper seOrderMapper;

    @Override
    public String creatOrder() throws UnsupportedEncodingException {
        return null;
    }

    @Override
    public String cancelOrder(String orderId) throws Exception {
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);

            SeOrder order=new SeOrder();
            order.setId(orderId);
            order = seOrderMapper.selectOne(order);

            MyConfig   config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            data.put("nonce_str", nonceStr);//随机字符串
            data.put("notify_url", "https://www.17inn.com/seventeen/WX/callBack");//异步回调api
            data.put("spbill_create_ip", "39.107.111.100");//支付ip
            data.put("out_trade_no", orderId);//商品订单号
            data.put("out_refund_no", "cancel"+orderId);//商品订单号
            data.put("total_fee", "1");//真实金额orderInfo.getFee()
            data.put("refund_fee", "1");//真实金额orderInfo.getFee()
            Map<String, String> refund = wxpay.refund(data);
            System.out.println(JSON.toJSONString(refund));



        return refund.get("result_code");
    }


}
