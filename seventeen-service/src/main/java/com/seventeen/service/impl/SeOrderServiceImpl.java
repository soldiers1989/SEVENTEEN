package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.OrderInfo;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.MyConfig;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.PageInfo;
import javafx.scene.input.DataFormat;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
public class SeOrderServiceImpl  implements SeOrderService  {
    private final Logger logger = LoggerFactory.getLogger(SeOrderServiceImpl.class);

    @Autowired
    private SeOrderMapper seOrderMapper;

    /**
     * @param status
     * @param remark
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result<List<OrderCenter>> getOrderList(SysUser sysUser,String status, String remark, PageInfo pageInfo, String startTime, String endTime) {
        Result<List<OrderCenter>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);

            String admin = "";
            /**
             * 小程序用户
             */
            if(sysUser.getRoleIds().contains("admin")){
                admin =  sysUser.getId();
            }

            ArrayList<OrderCenter> orderCenters = seOrderMapper.getSeOrders(admin,status, remark,startTime,endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> getOrderDetail(String orderId) {

        Result<OrderCenter> result = new Result<>();
        try {
            OrderCenter orderCenter = seOrderMapper.getOrderDetail(orderId);
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<OrderCenter>> noReplyOrderList(String reply,SysUser sysUser, PageInfo pageInfo) {

        Result<List<OrderCenter>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            String id = sysUser.getId();
            ArrayList<OrderCenter> orderCenters = seOrderMapper.getNoReplyOrderList(id,reply);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> noReplyOrder(String orderId, SysUser sysUser) {
        Result<OrderCenter> result = new Result<>();
        try {

            OrderCenter orderCenter = seOrderMapper.noReplyOrder(orderId,"0");
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-00");
        System.out.println(LocalDateTime.now().format(dateTimeFormatter));
    }

    @Override
    public ResponseEntity setOrder(SysUser sysUser, OrderInfo orderInfo) {
        Result result=new Result();
        SeOrder se=new SeOrder();
        //查出该分类下的空房












        //本系统业务下单生产订单ID
        String orderId = RandomStringUtils.randomAlphanumeric(24);
        System.out.println("订单号:" + orderId);
        //本次订单随机串
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);
        try {
            MyConfig config = new MyConfig();

            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("fee_type", "CNY");
            data.put("body", "17inn订房");//商品描述
            data.put("nonce_str", nonceStr);//随机字符串
            data.put("notify_url", "https://www.17inn.com/order/wx/payCallback");//异步回调api
            data.put("spbill_create_ip", "39.107.111.100");//支付ip
            data.put("out_trade_no", orderId);//商品订单号
            data.put("total_fee", orderInfo.getFee());//真实金额
            data.put("trade_type", "JSAPI");//JSAPI、h5调用
            data.put("openid", sysUser.getOpenid());//支付用户openid


            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println("---预订单返回--\n"+resp);
            //处理加密返回给前端
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String paySign="appId="+resp.get("appid")+"&nonceStr="+resp.get("nonce_str")+"&package=prepay_id="+resp.get("prepay_id")+"&signType=MD5&timeStamp="+timeStamp+"&key=1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy";
            System.out.println(paySign);
            paySign= MD5.MD5Encode(paySign,"UTF-8");

            Map<String,String > resMap=new HashMap<>();
            resMap.put("appId",resp.get("appid"));
            resMap.put("nonceStr",resp.get("nonce_str"));
            resMap.put("package","prepay_id="+resp.get("prepay_id"));
            resMap.put("paySign",paySign);
            resMap.put("timeStamp",timeStamp);


            result.setData(resMap);
            System.out.println(resMap);

        } catch (Exception e) {
            logger.error("下的错误",e);
        }
        return null;
    }

}