//package com.seveteen.controller;
//
//
//import com.seveteen.bean.SeApartment;
//import com.seveteen.bean.core.JwtAuthenticationResponse;
//import com.seveteen.core.Result;
//import com.seveteen.core.ResultGenerator;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Api(tags = "房间管理")
//public class ApartmentController {
//
//	private final Logger logger = LoggerFactory.getLogger(ApartmentController.class);
//
//	@GetMapping("/apartmentList")
//	@ApiOperation(value = "获取房间列表信息", response = JwtAuthenticationResponse.class)
//	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
//	public Result<JwtAuthenticationResponse> apartmentList(@RequestBody SeApartment seApartment) {
//
//		return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(refreshedToken));
//	}
//
//}