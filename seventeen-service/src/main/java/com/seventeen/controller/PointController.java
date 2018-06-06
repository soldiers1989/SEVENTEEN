//package com.seventeen.controller;
//
//
//import com.seventeen.bean.SeApartment;
//import com.seventeen.bean.SeApartmentDetail;
//import com.seventeen.bean.SeTag;
//import com.seventeen.core.Result;
//import com.seventeen.service.SeApartmentService;
//import com.seventeen.util.IDGenerator;
//import com.seventeen.util.PageInfo;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/room")
//@Api(tags = "房间管理")
//public class PointController {
//
//	private final Logger logger = LoggerFactory.getLogger(PointController.class);
//
//	@Autowired
//    private SeApartmentService seApartmentService;
//
//	@GetMapping
//	@ApiOperation(value = "获取房间列表信息")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity apartmentList(String status,String remark,PageInfo pageInfo) {
//        Result<List<SeApartment>> seApartments = seApartmentService.getSeApartments(status,remark,pageInfo);
//        return ResponseEntity.ok(seApartments);
//	}
//
//	@PostMapping
//	@ApiOperation(value = "新建房间")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity addApartment(@RequestBody SeApartment seApartment) {
//		Result<SeApartment> flag = seApartmentService.addApartment(seApartment);
//		return ResponseEntity.ok(flag);
//	}
//
//	@PostMapping("update")
//	@ApiOperation(value = "更新房间")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity updateApartment(@RequestBody SeApartment seApartment) {
//		Result<SeApartment> flag = seApartmentService.updateApartment(seApartment);
//		return ResponseEntity.ok(flag);
//	}
//
//	@GetMapping("/{apNum}/detail")
//	@ApiOperation(value = "获取房间信息")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity getApartmentDetail(@PathVariable String apNum) {
//		Result<SeApartmentDetail> seApartmentDetail = seApartmentService.getApartmentDetail(apNum);
//		return ResponseEntity.ok(seApartmentDetail);
//	}
//
//	@GetMapping("/{apNum}")
//	@ApiOperation(value = "根据ID校验房号存在不")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity apartmentCheck(@PathVariable String apNum) {
//		Result<Boolean> seApartments = seApartmentService.apartmentCheck(apNum);
//		return ResponseEntity.ok(seApartments);
//	}
//
//	@DeleteMapping
//	@ApiOperation(value = "删除房间")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity deleteApartment(@RequestParam String ids) {
//		Result<String> seApartments = seApartmentService.deleteApartment(ids);
//		return ResponseEntity.ok(seApartments);
//	}
//
//	@GetMapping("/tags")
//	@ApiOperation(value = "获取房间列表信息")
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public ResponseEntity getTags(String type, String name) {
//		Result<List<SeTag>> seApartments = seApartmentService.getTags(type,name);
//		seApartments.setMessage(IDGenerator.getId());
//		return ResponseEntity.ok(seApartments);
//	}
//}