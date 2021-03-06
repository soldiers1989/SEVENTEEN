package com.seventeen.controller;


import com.seventeen.bean.SeUserPoint;
import com.seventeen.bean.SeUserPointLog;
import com.seventeen.bean.UserPoint;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.service.SeUserPointService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@Api(tags = "积分管理")
@Slf4j
public class PointController {


	@Autowired
    private SeUserPointService seUserPointService;

	@GetMapping
	@ApiOperation(value = "获取用户积分列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity SeUserPointList(String remark,PageInfo pageInfo) {
        Result<List<UserPoint>> userPoints = seUserPointService.getSeUserPointList(remark,pageInfo);
        return ResponseEntity.ok(userPoints);
	}

	@GetMapping("/{id}/detail")
	@ApiOperation(value = "获取用户积分信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getSeUserPointDetail(@PathVariable String id) {
		Result<UserPoint> userPoint = seUserPointService.getSeUserPointDetail(id);
		return ResponseEntity.ok(userPoint);
	}

	@PostMapping
	@ApiOperation(value = "获取用户积分列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addPoint(@RequestBody  SeUserPointLog seUserPointLog) {
		Result result = seUserPointService.addPoint(seUserPointLog);
		return ResponseEntity.ok(result);
	}


    @GetMapping("/userPoint")
    @ApiOperation(value = "WX获取用户积分信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getSeUserPointDetail(@AuthenticationPrincipal SysUser sysUser) {
        Result result=new Result();
        SeUserPoint userPoint = seUserPointService.getWXUserPoint(sysUser.getId());
        Integer point=0;
        if(userPoint!=null)
            point=Integer.valueOf(userPoint.getPoint());
        result.setData(point);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/detailList")
    @ApiOperation(value = "获取用户积分信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getSeUserPointDetailList(@AuthenticationPrincipal SysUser sysUser) {
        Result<UserPoint> userPoint = seUserPointService.getSeUserPointDetailList(sysUser.getId());
        return ResponseEntity.ok(userPoint);
    }
}