package com.seventeen.controller;


import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@Api(tags = "订单")
public class OrderController {

	private final Logger logger = LoggerFactory.getLogger(OrderController.class);

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
        Result<List<OrderCenter>> seOrders = seOrderService.getOrderList(sysUser,status,remark,pageInfo,startTime,endTime);
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
	public ResponseEntity noReplyOrderList(String reply,PageInfo pageInfo,@AuthenticationPrincipal SysUser sysUser) {
		Result<List<OrderCenter>> seOrders = seOrderService.noReplyOrderList(reply,sysUser,pageInfo);
		return ResponseEntity.ok(seOrders);
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
}