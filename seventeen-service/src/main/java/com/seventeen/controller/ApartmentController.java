package com.seventeen.controller;


import com.seventeen.bean.SeApartment;
import com.seventeen.bean.core.JwtAuthenticationResponse;
import com.seventeen.core.Result;
import com.seventeen.core.ResultGenerator;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@Api(tags = "房间管理")
public class ApartmentController {

	private final Logger logger = LoggerFactory.getLogger(ApartmentController.class);

	@Autowired
    private SeApartmentService seApartmentService;

	@GetMapping
	@ApiOperation(value = "获取房间列表信息", response = JwtAuthenticationResponse.class)
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public Result<JwtAuthenticationResponse> apartmentList(String status,String remark,PageInfo pageInfo) {
        Result<List<SeApartment>> seApartments = seApartmentService.getSeApartments(status,remark,pageInfo);
        return ResultGenerator.genSuccessResult(seApartments);
	}

	@DeleteMapping
	@ApiOperation(value = "获取房间列表信息", response = JwtAuthenticationResponse.class)
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public Result<JwtAuthenticationResponse> deleteApartment(@RequestParam String ids) {
		Result<String> seApartments = seApartmentService.deleteApartment(ids);
		return ResultGenerator.genSuccessResult(seApartments);
	}

}