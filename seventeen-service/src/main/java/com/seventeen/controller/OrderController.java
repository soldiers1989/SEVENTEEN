package com.seventeen.controller;


import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.OrderInfo;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Api(tags = "订单")
@Slf4j
public class OrderController {


	@Autowired
    private SeOrderService seOrderService;
    @Autowired
    private WxPay wxPay;


	/**
	 * 下订和退订还没写接口
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
    public ResponseEntity OrderList(String status,String remark,String startTime,String endTime,PageInfo pageInfo,@AuthenticationPrincipal SysUser sysUser) {
        Result<List<OrderCenter>> seOrders = seOrderService.getOrderList(sysUser, status, remark, pageInfo, startTime, endTime);
        return ResponseEntity.ok(seOrders);
    }

    @GetMapping("/wx")
	@ApiOperation(value = "获取未评价订单列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity noReplyOrderList(String reply,PageInfo pageInfo,@AuthenticationPrincipal SysUser sysUser) {
		Result<List<OrderCenter>> seOrders = seOrderService.noReplyOrderList(reply,sysUser,pageInfo);
		return ResponseEntity.ok(seOrders);
	}


    @DeleteMapping("/wx")
    @ApiOperation(value = "删除订单")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity deleteOrderByWx(@RequestParam String id,@AuthenticationPrincipal SysUser sysUser) {
        Result<String> result = seOrderService.deleteOrderByWx(id,sysUser);
        return ResponseEntity.ok(result);
    }

	@GetMapping("/wx/{orderId}/detail")
	@ApiOperation(value = "获取未评价订单列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity noReplyOrder(@PathVariable String orderId,@AuthenticationPrincipal SysUser sysUser) {
		Result<OrderCenter> seOrder = seOrderService.noReplyOrder(orderId,sysUser);
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
    public ResponseEntity setOrder(@AuthenticationPrincipal SysUser sysUser, @RequestBody OrderInfo orderInfo) {
        System.out.println(orderInfo);
        ResponseEntity entity= seOrderService.setOrder(sysUser,orderInfo);
        return entity;
    }

	@GetMapping("/wx/getOrderDate")
	@ApiOperation(value = "获取订单满房时间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getOrderDate(@AuthenticationPrincipal SysUser sysUser,String roomType) {
		Result result= seOrderService.getOrderDate(roomType,sysUser);
		return ResponseEntity.ok(result);
	}

	/**
	 * 每月1号凌晨插入月历数据
	 */
	@Scheduled(cron = "0 0 0 1 * ? ")
	public void timerCron() {
		seOrderService.addOrderCalendar();
	}

}