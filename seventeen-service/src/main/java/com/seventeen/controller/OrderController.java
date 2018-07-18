package com.seventeen.controller;


import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.pay.wx.bean.WxOrderBean;
import com.seventeen.pay.wx.service.WxPay;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.XMLUtil;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    @Autowired
    private WxPay wxPay;


    @GetMapping
    @ApiOperation(value = "获取订单列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity OrderList(String status, String remark, String startTime, String endTime, PageInfo pageInfo, @AuthenticationPrincipal SysUser sysUser) {
        Result<List<OrderCenter>> seOrders = seOrderService.getOrderList(sysUser, status, remark, pageInfo, startTime, endTime);
        return ResponseEntity.ok(seOrders);
    }

    @GetMapping("/{orderId}/detail")
    @ApiOperation(value = "获取订单详细信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getOrderDetail(@PathVariable String orderId) {
        Result<OrderCenter> orderCenter = seOrderService.getOrderDetail(orderId);
        return ResponseEntity.ok(orderCenter);
    }


    /**
     * 微信创建订单
     *
     * @param sysUser
     * @param order
     * @return
     */
    @PostMapping("/wx/creatOrder")
    @ApiOperation(value = "获取订单详细信息")
    public ResponseEntity wxCreatOrder(@AuthenticationPrincipal SysUser sysUser, @RequestBody SeOrder order) {
        //创建订单
        //微信订单
        WxOrderBean rb = new WxOrderBean();
        rb.setDevice_info("WEB");
        rb.setNonce_str(RandomStringUtils.randomAlphanumeric(32));

        System.out.println("支付申请开始");



//        wxPay.creatOrder(rb);
        return null;
    }

    public static void main(String[] args) {
        //System.out.println();
        
        String appid="wxe6f9450a01336cb9";
        String body="成都市";
        String mch_id="1509646201";
        String nonce_str=RandomStringUtils.randomAlphanumeric(32);//随机字符串
        String notify_url="https://www.17inn.com/order/wx/payCallback";
        String openid="oBixG4yTazP6-fwDNZQtf5MObxAo";
        String out_trade_no="ABVW2";
        String spbill_create_ip="39.107.111.100";//终端的ip
        String total_fee="1";//总金额 最低为一块钱 必须是整数
        String trade_type="JSAPI";

        String str="appid="+appid+"&body="+body+"&mch_id="+mch_id+"&nonce_str="+nonce_str+"&notify_url="+notify_url+"&openid="+openid+
                "&out_trade_no="+out_trade_no+"&spbill_create_ip="+spbill_create_ip+"&total_fee="+total_fee+"&trade_type="+trade_type+"&key=UAbG1gXL5J7JaBgMUPugCV2z98qEpgXS";

        String sign=MD5.MD5Encode(str,"UTF-8").toUpperCase();

        String xml = "<xml>" +
                "<appid>" + appid + "</appid>" +
                "<body>" + body + "</body>" +
                "<mch_id>" + mch_id + "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<openid>" + openid + "</openid>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>" +
                "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>" +
                "<total_fee>" + total_fee + "</total_fee>" +
                "<trade_type>"+trade_type+"</trade_type>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";

        System.out.println(xml);
        testPost("https://api.mch.weixin.qq.com/pay/unifiedorder",xml);

    }

    static void testPost(String urlStr,String xml) {
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");

            OutputStreamWriter out = new OutputStreamWriter(con
                    .getOutputStream());
            String xmlInfo = xml;
            System.out.println("urlStr=" + urlStr);
            System.out.println("xmlInfo=" + xmlInfo);
            out.write(new String(xmlInfo.getBytes("ISO-8859-1")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con
                    .getInputStream()));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付宝创建订单
     *
     * @param sysUser
     * @param order
     * @return
     */
    @PostMapping("/ali/creatOrder")
    @ApiOperation(value = "获取订单详细信息")
    public ResponseEntity aliCreatOrder(@AuthenticationPrincipal SysUser sysUser, @RequestBody SeOrder order) {
        //创建订单
        //支付宝订单

        return null;
    }


    @PostMapping("/wx/payCallback")
    @ApiOperation(value = "微信回掉")
    public void payCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("微信回调接口方法 start");
        logger.info("微信回调接口 操作逻辑 start");
        String inputLine = "";
        String notityXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            //关闭流
            request.getReader().close();
            logger.info("微信回调内容信息：" + notityXml);
            //解析成Map
            Map<String, String> map = XMLUtil.doXMLParse(notityXml);
            logger.info("微信回调内容信息：" + JSON.toJSONString(map));
            //判断 支付是否成功
//            if("SUCCESS".equals(map.get("result_code"))){
//                logger.info("微信回调返回是否支付成功：是");
//                //获得 返回的商户订单号
//                String outTradeNo = map.get("out_trade_no");
//                logger.info("微信回调返回商户订单号："+outTradeNo);
//                //访问DB
//                WechatAppletGolfPayInfo payInfo = appletGolfPayInfoMapper.selectByPrimaryKey(outTradeNo);
//                logger.info("微信回调 根据订单号查询订单状态："+payInfo.getPayStatus());
//                if("0".equals(payInfo.getPayStatus())){
//                    //修改支付状态
//                    payInfo.setPayStatus("1");
//                    //更新Bean
//                    int sqlRow = appletGolfPayInfoMapper.updateByPrimaryKey(payInfo);
//                    //判断 是否更新成功
//                    if(sqlRow == 1){
//                        logger.info("微信回调  订单号："+outTradeNo +",修改状态成功");
//                        //封装 返回值
//                        StringBuffer buffer = new StringBuffer();
//                        buffer.append("<xml>");
//                        buffer.append("<return_code>SUCCESS</return_code>");
//                        buffer.append("<return_msg>OK</return_msg>");
//                        buffer.append("<xml>");
//
//                        //给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
//                        PrintWriter writer = response.getWriter();
//                        //返回
//                        writer.print(buffer.toString());
//                    }
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//
//	@PostMapping
//	@ApiOperation(value = "创建优惠卷")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity addOrder(@RequestBody SeOrder seOrder) {
//		Result<SeOrder> flag = seOrderService.addOrder(seOrder);
//		return ResponseEntity.ok(flag);
//	}
//
//	@DeleteMapping
//	@ApiOperation(value = "删除优惠卷")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity deleteOrder(@RequestParam String ids) {
//		Result<String> flag = seOrderService.deleteOrder(ids);
//		return ResponseEntity.ok(flag);
//	}
//
//	@GetMapping("/{OrderId}/detail")
//	@ApiOperation(value = "获取优惠卷信息")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity getOrderDetail(@PathVariable String OrderId) {
//		Result<SeOrder> OrderDetail = seOrderService.getOrderDetail(OrderId);
//		return ResponseEntity.ok(OrderDetail);
//	}
//
//
//	@PostMapping("update")
//	@ApiOperation(value = "更新优惠卷")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity updateOrder(@RequestBody SeOrder seOrder) {
//		Result<SeOrder> flag = seOrderService.updateOrder(seOrder);
//		return ResponseEntity.ok(flag);
//	}
//
//	@GetMapping("OrderLog")
//	@ApiOperation(value = "获取优惠卷使用记录")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity getOrderLog(String status,String remark,String startTime,String endTime,PageInfo pageInfo) {
//		Result<List<OrderLog>> OrderLogs = seOrderService.getOrderLog(status,remark,startTime,endTime,pageInfo);
//		return ResponseEntity.ok(OrderLogs);
//	}
//
//	/**
//	 * 每天凌晨更新优惠卷状态
//	 */
//	@Scheduled(cron = "0 0 0 * * ? ")
//	public void timerCron() {
//		seOrderService.updateOrderStatus();
//	}

}