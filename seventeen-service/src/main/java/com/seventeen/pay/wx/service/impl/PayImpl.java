package com.seventeen.pay.wx.service.impl;

import com.github.wxpay.sdk.WXPay;
import com.seventeen.common.utils.HttpUtil;
import com.seventeen.pay.wx.util.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

public class PayImpl {

    public static void main(String[] args) throws Exception {
        String nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<>();
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("body", "abc");//商品描述
        data.put("nonce_str", nonceStr);//随机字符串
        data.put("notify_url", "https://www.17inn.com/order/wx/payCallback");//异步回调api
        data.put("spbill_create_ip", "39.107.111.100");//支付ip
        data.put("out_trade_no", "ABV001");//商品订单号
        data.put("total_fee", (int) 1 + "");//真实金额
        data.put("trade_type", "JSAPI");//JSAPI、h5调用
        data.put("sign_type", "HMAC-SHA256");//JSAPI、h5调用
        data.put("openid", "oBixG4yTazP6-fwDNZQtf5MObxAo");//支付用户openid

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        PayImpl p = new PayImpl();
//        p.creatOrder();

    }

    public void creatOrder() {
        String nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";//暂时不变

        // 加密，这里只列举必填字段
        Map<String, String> map = new HashMap<String, String>();
        map.put("body", "abc");//商品描述
        map.put("mch_id", "1509646201");//商户平台id
        map.put("appid", "wxe6f9450a01336cb9");//公众号id
        map.put("nonce_str", nonceStr);//随机字符串
        map.put("notify_url", "https://www.17inn.com/order/wx/payCallback");//异步回调api
        map.put("spbill_create_ip", "39.107.111.100");//支付ip
        map.put("out_trade_no", "ABV001");//商品订单号
        map.put("total_fee", (int) 1 + "");//真实金额
        map.put("trade_type", "JSAPI");//JSAPI、h5调用
        map.put("openid", "oBixG4yTazP6-fwDNZQtf5MObxAo");//支付用户openid

        String sign = WxPaySignatureUtils.signature(map, "UAbG1gXL5J7JaBgMUPugCV2z98qEpgXS");

        String xml = "<xml>" +
                "<appid>" + map.get("appid") + "</appid>" +
                "<body>" + map.get("body") + "</body>" +
                "<mch_id>" + map.get("mch_id") + "</mch_id>" +
                "<nonce_str>" + nonceStr + "</nonce_str>" +
                "<notify_url>" + map.get("notify_url") + "</notify_url>" +
                "<openid>" + map.get("openid") + "</openid>" +
                "<out_trade_no>" + map.get("out_trade_no") + "</out_trade_no>" +
                "<spbill_create_ip>" + map.get("spbill_create_ip") + "</spbill_create_ip>" +
                "<total_fee>" + 1 + "</total_fee>" +
                "<trade_type>JSAPI</trade_type>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";

        System.out.println("发送给微信的报文：" + xml);
        System.out.println("加密后的的签名字符串：" + sign);
        
// 请求
        String response = "";
        try {
            response = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", xml.trim());
        } catch (Exception e) {
            //TODO
        }
        System.out.println("请求/pay/unifiedorder下单接口后返回数据：" + response);
////处理请求结果
//        XStream s = new XStream(new DomDriver());
//        s.alias("xml", WechatOrder.class);
//        WechatOrder order = (WechatOrder) s.fromXML(response);
//
//        if ("SUCCESS".equals(order.getReturn_code()) && "SUCCESS".equals(order.getResult_code())) {
//            System.out.println("微信支付统一下单请求成功，获得的Prepay_id是：" + order.getPrepay_id());
//        } else {
//            System.out.println("微信支付统一下单请求错误：" + order.getReturn_msg() + order.getErr_code());
//            //TODO
//        }


//        String nonce_str = RandomStringUtils.randomAlphanumeric(32);
//        Map<String, String> map = new TreeMap<>();
//        map.put("appid", "wxe6f9450a01336cb9");
//        map.put("mch_id", "1509646201");
//        map.put("nonce_str", nonce_str);
//        map.put("body", "测试一下");
//        map.put("out_trade_no", "ABV001");
//        map.put("total_fee", "1");
//        map.put("spbill_create_ip", "39.107.111.100");
//        map.put("notify_url", "https://www.17inn.com/order/wx/payCallback");
//        map.put("trade_type", "JSAPI");
//        map.put("openid", "oBixG4yTazP6");
//
//        String sign = WxPaySignatureUtils.signature(map, "tWe8yq3SVen03rkrOu4VAlDs8UiEsQKD");
//        System.out.println(sign);

//        Set<String> strings = map.keySet();
//        List<String> list = new ArrayList<>();
//        list.addAll(strings);
//        Collections.sort(list);
//        //排序
//        StringBuffer sb = new StringBuffer();
//        for (String s : list) {
//            sb.append(s);
//            sb.append("=");
//            sb.append(map.get(s));
//            sb.append("&");
//        }
//        sb.append("key=tWe8yq3SVen03rkrOu4VAlDs8UiEsQKD");
//        System.out.println(sb.toString());
//        //加密
//        String sign = MD5.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
//        System.out.println(sign);
//        map.put("sign", sign);
////        String requestXML = PayCommonUtil.getRequestXml(map);
//        String xml = "<xml>" +
//                "<appid>"+map.get("appid")+"</appid> " +
//                "<mch_id>"+map.get("mch_id")+"</mch_id>" +
//                "<nonce_str>"+map.get("nonce_str")+"</nonce_str>" +
//                "<body>"+map.get("body")+"</body>" +
//                "<out_trade_no>"+map.get("out_trade_no")+"</out_trade_no>" +
//                "<total_fee>"+map.get("total_fee")+"</total_fee>" +
//                "<spbill_create_ip>"+map.get("spbill_create_ip")+"</spbill_create_ip>" +
//                "<notify_url>"+map.get("notify_url")+"</notify_url>" +
//                "<trade_type>"+map.get("trade_type")+"</trade_type>" +
//                "<openid>"+map.get("openid")+"</openid>" +
//                "<sign>"+map.get("sign")+"</sign>" +
//                "</xml>";
//
//
//
//        System.out.println(xml);

//        String resXml = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", xml.trim());
//        System.out.println(resXml);
    }

}
