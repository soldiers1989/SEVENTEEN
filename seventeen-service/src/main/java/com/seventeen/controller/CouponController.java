package com.seventeen.controller;


import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import com.seventeen.core.Result;
import com.seventeen.service.SeCouponService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@Api(tags = "优惠券")
public class CouponController {

	private final Logger logger = LoggerFactory.getLogger(CouponController.class);

	@Autowired
    private SeCouponService seCouponService;

	@GetMapping
	@ApiOperation(value = "获取优惠券列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity couponList(String status,String remark,PageInfo pageInfo) {
        Result<List<SeCoupon>> seCoupons = seCouponService.getCouponList(status,remark,pageInfo);
        return ResponseEntity.ok(seCoupons);
	}

	@GetMapping("/wx")
	@ApiOperation(value = "微信端获取优惠券列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity couponListWx(String status,String remark,PageInfo pageInfo) {
		Result<List<SeCoupon>> seCoupons = seCouponService.couponListWx(status,pageInfo);
		return ResponseEntity.ok(seCoupons);
	}


	@PostMapping
	@ApiOperation(value = "创建优惠卷")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addCoupon(@RequestBody SeCoupon seCoupon) {
		Result<SeCoupon> flag = seCouponService.addCoupon(seCoupon);
		return ResponseEntity.ok(flag);
	}

	@DeleteMapping
	@ApiOperation(value = "删除优惠卷")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity deleteCoupon(@RequestParam String ids) {
		Result<String> flag = seCouponService.deleteCoupon(ids);
		return ResponseEntity.ok(flag);
	}

	@GetMapping("/{couponId}/detail")
	@ApiOperation(value = "获取优惠卷信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getCouponDetail(@PathVariable String couponId) {
		Result<SeCoupon> couponDetail = seCouponService.getCouponDetail(couponId);
		return ResponseEntity.ok(couponDetail);
	}


	@PostMapping("update")
	@ApiOperation(value = "更新优惠卷")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updateCoupon(@RequestBody SeCoupon seCoupon) {
		Result<SeCoupon> flag = seCouponService.updateCoupon(seCoupon);
		return ResponseEntity.ok(flag);
	}

	@GetMapping("couponLog")
	@ApiOperation(value = "获取优惠卷使用记录")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getCouponLog(String status,String remark,String startTime,String endTime,PageInfo pageInfo) {
		Result<List<CouponLog>> couponLogs = seCouponService.getCouponLog(status,remark,startTime,endTime,pageInfo);
		return ResponseEntity.ok(couponLogs);
	}

	/**
	 * 每天凌晨更新优惠卷状态
	 */
	@Scheduled(cron = "0 0 0 * * ? ")
	public void timerCron() {
		seCouponService.updateCouponStatus();
	}

}