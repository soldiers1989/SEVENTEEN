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

	@GetMapping
	@ApiOperation(value = "获取订单列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity OrderList(String status,String remark,String startTime,String endTime,PageInfo pageInfo,@AuthenticationPrincipal SysUser sysUser) {
        Result<List<OrderCenter>> seOrders = seOrderService.getOrderList(sysUser,status,remark,pageInfo,startTime,endTime);
        return ResponseEntity.ok(seOrders);
	}

	@GetMapping("/{orderId}/detail")
	@ApiOperation(value = "获取订单详细信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getOrderDetail(@PathVariable String orderId) {
		Result<OrderCenter> orderCenter = seOrderService.getOrderDetail(orderId);
		return ResponseEntity.ok(orderCenter);
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