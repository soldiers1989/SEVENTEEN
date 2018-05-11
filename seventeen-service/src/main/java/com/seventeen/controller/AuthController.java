package com.seventeen.controller;


import com.seventeen.bean.core.JwtAuthenticationRequest;
import com.seventeen.bean.core.JwtAuthenticationResponse;
import com.seventeen.core.Result;
import com.seventeen.core.ResultGenerator;
import com.seventeen.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "登录认证")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	@ApiOperation(value = "根据账号和密码获取token，token在10小时内有效,过期后请再次调用。", response = JwtAuthenticationResponse.class)
	public Result<JwtAuthenticationResponse> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
		final String token = authService.login(authenticationRequest.getUsername(),
				authenticationRequest.getPassword());
		// Return the token
		return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(token));
	}

	@GetMapping("/refresh")
	@ApiOperation(value = "根据有效的旧token获取新的token，失效的token无法获取到新token。", response = JwtAuthenticationResponse.class)
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public Result<JwtAuthenticationResponse> refreshAndGetAuthenticationToken(
			@RequestHeader(value = "Authorization") String token) throws AuthenticationException {
		String refreshedToken = authService.refresh(token);
		return ResultGenerator.genSuccessResult(new JwtAuthenticationResponse(refreshedToken));
	}

}