package com.seventeen.controller;


import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.pay.wx.service.WxPay;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.MyConfig;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Api(tags = "订单")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private SeOrderService seOrderService;
//    @Autowired
//    private WxPay wxPay;


    /**
     * 下订和退订还没写接口
     *
     * @param status
     * @param remark
     * @param startTime
     * @param endTime
     * @param pageInfo
     * @param sysUser
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获取订单列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity OrderList(String status, String remark, String startTime, String endTime, PageInfo pageInfo, @AuthenticationPrincipal SysUser sysUser) {
        Result<List<OrderCenter>> seOrders = seOrderService.getOrderList(sysUser, status, remark, pageInfo, startTime, endTime);
        return ResponseEntity.ok(seOrders);
    }

    @GetMapping("/wx")
    @ApiOperation(value = "获取未评价订单列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity noReplyOrderList(String reply, PageInfo pageInfo, @AuthenticationPrincipal SysUser sysUser) {
        Result<List<OrderCenter>> seOrders = seOrderService.noReplyOrderList(reply, sysUser, pageInfo);
        return ResponseEntity.ok(seOrders);
    }

    @GetMapping("/wx/{orderId}/detail")
    @ApiOperation(value = "获取未评价订单列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity noReplyOrder(@PathVariable String orderId, @AuthenticationPrincipal SysUser sysUser) {
        Result<OrderCenter> seOrder = seOrderService.noReplyOrder(orderId, sysUser);
        return ResponseEntity.ok(seOrder);
    }

    @GetMapping("/{orderId}/detail")
    @ApiOperation(value = "获取订单详细信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getOrderDetail(@PathVariable String orderId) {
        Result<OrderCenter> orderCenter = seOrderService.getOrderDetail(orderId);
        return ResponseEntity.ok(orderCenter);
    }

    @PostMapping("/wx/setOrder")
    @ApiOperation(value = "获取订单详细信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity setOrder(@AuthenticationPrincipal SysUser sysUser, @RequestParam String roomId, @RequestParam String fee) {
        Result result=new Result();
        //本系统业务下单生产订单ID
        String orderId = RandomStringUtils.randomAlphanumeric(24);
        System.out.println("订单号:" + orderId);
        //本次订单随机串
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);
        try {
            MyConfig  config = new MyConfig();

            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("fee_type", "CNY");
            data.put("body", "17inn订房");//商品描述
            data.put("nonce_str", nonceStr);//随机字符串
            data.put("notify_url", "https://www.17inn.com/order/wx/payCallback");//异步回调api
            data.put("spbill_create_ip", "39.107.111.100");//支付ip
            data.put("out_trade_no", orderId);//商品订单号
            data.put("total_fee", fee);//真实金额
            data.put("trade_type", "JSAPI");//JSAPI、h5调用
            data.put("openid", sysUser.getOpenid());//支付用户openid


            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println("---预订单返回--\n"+resp);
            //处理加密返回给前端
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String paySign="appId="+resp.get("appid")+"&nonceStr="+resp.get("nonce_str")+"&package=prepay_id="+resp.get("prepay_id")+"&signType=MD5&timeStamp="+timeStamp+"&key=1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy";
            System.out.println(paySign);
            paySign=MD5.MD5Encode(paySign,"UTF-8");

            Map<String,String > resMap=new HashMap<>();
            resMap.put("appId",resp.get("appid"));
            resMap.put("nonceStr",resp.get("nonce_str"));
            resMap.put("package","prepay_id="+resp.get("prepay_id"));
            resMap.put("paySign",paySign);
            resMap.put("timeStamp",timeStamp);


            result.setData(resMap);
            System.out.println(resMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

       return ResponseEntity.ok(result);
    }

    public static void main(String[] args) {
        System.out.println(MD5.MD5Encode("appId=wxe6f9450a01336cb9&eIHFWApv9H0qo9d8&package=prepay_id=wx19094959173915fe07f004e13298463687&signType=MD5&timeStamp=1531964994149&key=1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy","utf-8"));
    }

}