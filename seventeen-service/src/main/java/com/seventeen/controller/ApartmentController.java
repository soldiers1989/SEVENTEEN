package com.seventeen.controller;


import com.seventeen.bean.*;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@Api(tags = "房间管理")
public class ApartmentController {

	private final Logger logger = LoggerFactory.getLogger(ApartmentController.class);

	@Autowired
    private SeApartmentService seApartmentService;


	/**
	 *  漏做结束入住时间排序，房间状态修改没弄好
	 * @param status
	 * @param remark
	 * @param roomType
	 * @param pageInfo
	 * @return
	 */
	@GetMapping
	@ApiOperation(value = "获取房间列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity apartmentList(String status,String remark,String roomType,PageInfo pageInfo) {
        Result<List<SeApartment>> seApartments = seApartmentService.getSeApartments(status,roomType,remark,pageInfo);
        return ResponseEntity.ok(seApartments);
	}

	@PostMapping
	@ApiOperation(value = "新建房间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addApartment(@RequestBody SeApartment seApartment) {
		Result<SeApartment> flag = seApartmentService.addApartment(seApartment);
		return ResponseEntity.ok(flag);
	}

	@PostMapping("update")
	@ApiOperation(value = "更新房间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updateApartment(@RequestBody SeApartment seApartment) {
		Result<SeApartment> flag = seApartmentService.updateApartment(seApartment);
		return ResponseEntity.ok(flag);
	}

	@GetMapping("/{apNum}/detail")
	@ApiOperation(value = "获取房间信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getApartmentDetail(@PathVariable String apNum) {
		Result<SeApartmentDetail> seApartmentDetail = seApartmentService.getApartmentDetail(apNum);
		return ResponseEntity.ok(seApartmentDetail);
	}

	@GetMapping("/{apNum}")
	@ApiOperation(value = "根据ID校验房号存在不")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity apartmentCheck(@PathVariable String apNum) {
		Result<Boolean> seApartments = seApartmentService.apartmentCheck(apNum);
		return ResponseEntity.ok(seApartments);
	}

	@DeleteMapping
	@ApiOperation(value = "删除房间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity deleteApartment(@RequestParam String ids) {
		Result<String> seApartments = seApartmentService.deleteApartment(ids);
		return ResponseEntity.ok(seApartments);
	}

	@GetMapping("/tags")
	@ApiOperation(value = "获取tag信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getTags(String type, String name) {
		Result<List<SeTag>> seApartments = seApartmentService.getTags(type,name);
		seApartments.setMessage(IDGenerator.getId());
		return ResponseEntity.ok(seApartments);
	}

	@GetMapping("/tag")
	@ApiOperation(value = "获取tag详细信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getTag(String id) {
		Result result = seApartmentService.getTag(id);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/goods")
	@ApiOperation(value = "获取tag信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getGoods(String roomType) {
		Result<List<SeTag>> seApartments = seApartmentService.getGoods(roomType);
		seApartments.setMessage(IDGenerator.getId());
		return ResponseEntity.ok(seApartments);
	}

	@PostMapping("/tags")
	@ApiOperation(value = "插入tag信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addTags(@RequestBody SeTag seTag) {
		Result seApartments = seApartmentService.addTags(seTag);
		return ResponseEntity.ok(seApartments);
	}

	@PostMapping("/tags/update")
	@ApiOperation(value = "插入tag信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updateTags(@RequestBody SeTag seTag) {
		Result result = seApartmentService.updateTags(seTag);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/clean")
	@ApiOperation(value = "获取清洁列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getClean(String apNum,PageInfo pageInfo) {
		Result<List<ApartmentClean>> result = seApartmentService.getClean(apNum,pageInfo);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/clean/cleanToday")
	@ApiOperation(value = "获取清洁列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getCleanToday() {
		Result<List<ApartmentClean>> result = seApartmentService.getCleanToday();
		return ResponseEntity.ok(result);
	}

	@PostMapping("/clean")
	@ApiOperation(value = "提交清洁申请")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addClean(@RequestBody SeApartmentClean seApartmentClean,@AuthenticationPrincipal SysUser sysUser) {
		Result result = seApartmentService.addClean(seApartmentClean,sysUser);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/clean")
	@ApiOperation(value = "清洁房间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updateCleanStatus(@RequestBody SeApartmentClean seApartmentClean,@AuthenticationPrincipal SysUser sysUser) {
		Result result = seApartmentService.updateCleanStatus(seApartmentClean,sysUser);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/tags")
	@ApiOperation(value = "删除tag")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity deleteTag(@RequestParam String ids) {
		Result<String> seApartments = seApartmentService.deleteTag(ids);
		return ResponseEntity.ok(seApartments);
	}

	@PostMapping("/priceType")
	@ApiOperation(value = "提交价格类型")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addPriceType(@RequestBody ApartmentPriceRoom ruleRoomForm) {
		Result result = seApartmentService.addPriceType(ruleRoomForm);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/priceType")
	@ApiOperation(value = "获取价格类型")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getPriceType() {
		Result result = seApartmentService.getPriceType();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/priceType/{roomTypeId}/detail")
	@ApiOperation(value = "获取价格细节")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getPriceTypeDetail(@PathVariable String roomTypeId) {
		Result result = seApartmentService.getPriceTypeDetail(roomTypeId);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/priceType/update")
	@ApiOperation(value = "更新房间价格类型信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updatePriceType(@RequestBody ApartmentPriceRoom ruleRoomForm) {
		Result result = seApartmentService.updatePriceType(ruleRoomForm);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/wx/getApartmentByTime")
	@ApiOperation(value = "获取时间段内可用房间")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updatePriceType(String startTime,String endTime,String roomType) {
        startTime = startTime + " 14:00:00";
        endTime = endTime + " 12:00:00";
		Result result = seApartmentService.getApartmentByTime(startTime,endTime,roomType);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/advise")
	@ApiOperation(value = "获取意见投诉列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getAdviseList(String startTime,String endTime,PageInfo pageInfo) {
		Result<List<SeAdvise>> seApartments = seApartmentService.getAdviseList(startTime,endTime,pageInfo);
		return ResponseEntity.ok(seApartments);
	}

	@PostMapping("/wx/advise")
	@ApiOperation(value = "微信端添加意见投诉")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addAdvise(@RequestBody  SeAdvise seAdvise,@AuthenticationPrincipal SysUser sysUser) {
		Result result = seApartmentService.addAdvise(seAdvise,sysUser);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/wx/img")
	@ApiOperation(value = "获取房间图片列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getAapartmentImgs(String roomType) {
		Result<List<String>> seApartments = seApartmentService.getAapartmentImgs(roomType);
		return ResponseEntity.ok(seApartments);
	}
}