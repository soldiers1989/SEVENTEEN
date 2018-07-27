package com.seventeen.controller.WxApp;

import com.seventeen.pay.wx.util.PayCommonUtil;
import com.seventeen.pay.wx.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 小程序支付回调接口
 * lzb- 2018年7月19日10:22:25
 */
@RestController
@Slf4j
@RequestMapping("/WX/callBack")
public class WXConntroller  {

    @RequestMapping(produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String payNotifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader reader = null;

        reader = request.getReader();
        String line = "";
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        log.info("----接收到的数据如下：---" + xmlString);
        Map<String, String> map = new HashMap<>();
        String result_code = "";
        String return_code = "";
        String out_trade_no = "";
        map = XMLUtil.doXMLParse(xmlString);
        result_code = map.get("result_code");
        out_trade_no = map.get("out_trade_no");
        return_code = map.get("return_code");

        if (checkSign(xmlString)) {
//            this.memberOrderService.updateOrderInfo(out_trade_no);
//            MemberOrder memberOrder = memberOrderService.get(out_trade_no);
//            String couponId = memberOrder.getCouponId();
//            if (StringUtils.isNotEmpty(couponId)) {
//                memberCouponService.updateStatus(couponId);
//            }
            return returnXML(result_code);
        } else {
            return returnXML("FAIL");
        }


    }

    private boolean checkSign(String xmlString) {

        Map<String, String> map = null;

        try {

            map = XMLUtil.doXMLParse(xmlString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String signFromAPIResponse = map.get("sign").toString();

        if (signFromAPIResponse == "" || signFromAPIResponse == null) {

            System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改!!!");

            return false;

        }
        System.out.println("服务器回包里面的签名是:" + signFromAPIResponse);

        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名

        map.put("sign", "");

        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较

        String signForAPIResponse = getSign(map);

        if (!signForAPIResponse.equals(signFromAPIResponse)) {

            //签名验不过，表示这个API返回的数据有可能已经被篡改了

            System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改!!! signForAPIResponse生成的签名为" + signForAPIResponse);

            return false;

        }

        System.out.println("恭喜，API返回的数据签名验证通过!!!");

        return true;

    }


    private String returnXML(String return_code) {

        return "<xml><return_code><![CDATA["

                + return_code

                + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

    public String getSign(Map<String, String> map) {
        SortedMap<Object, Object> signParams = new TreeMap<Object, Object>();
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            signParams.put(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        signParams.remove("sign");
        String sign = PayCommonUtil.createSign("UTF-8", signParams,"1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy");

        return sign;
    }

}
