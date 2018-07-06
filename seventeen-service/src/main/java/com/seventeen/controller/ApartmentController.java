package com.seventeen.controller;


import com.seventeen.bean.ApartmentPriceRoom;
import com.seventeen.bean.SeApartment;
import com.seventeen.bean.SeApartmentDetail;
import com.seventeen.bean.SeTag;
import com.seventeen.core.Result;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/tags")
	@ApiOperation(value = "插入tag信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addTags(@RequestBody SeTag seTag) {
		Result seApartments = seApartmentService.addTags(seTag);
		return ResponseEntity.ok(seApartments);
	}
	@DeleteMapping("/tags")
	@ApiOperation(value = "删除tag")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity deleteTag(@RequestParam String ids) {
		Result<String> seApartments = seApartmentService.deleteTag(ids);
		return ResponseEntity.ok(seApartments);
	}



	@PostMapping("/priceType")
	@ApiOperation(value = "插入房间价格类型信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addPriceType(@RequestBody ApartmentPriceRoom ruleRoomForm) {
		Result result = seApartmentService.addPriceType(ruleRoomForm);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/priceType")
	@ApiOperation(value = "获取房间价格类型信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getPriceType() {
		Result result = seApartmentService.getPriceType();
		return ResponseEntity.ok(result);
	}
}