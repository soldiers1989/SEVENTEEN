package com.seventeen.controller;


import com.seventeen.bean.AssessPoint;
import com.seventeen.bean.SeAssessByWx;
import com.seventeen.bean.SeAssessContent;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.service.SeAssessService;
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
@RequestMapping("/assess")
@Api(tags = "评价中心")
@Slf4j
public class AssessController {


    @Autowired
    private SeAssessService seAssessService;

    @GetMapping
    @ApiOperation(value = "获取评价列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getAssessList(String isCheck, String remark, String startTime, String endTime, PageInfo pageInfo, @AuthenticationPrincipal SysUser sysUser) {
        Result<List<AssessPoint>> SeAssesss = seAssessService.getAssessList(isCheck, remark, pageInfo, startTime, endTime);
        return ResponseEntity.ok(SeAssesss);
    }

    @GetMapping("/wx")
    @ApiOperation(value = "微信端获取已评价列表信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getreplyAssessList( PageInfo pageInfo, @AuthenticationPrincipal SysUser sysUser) {
        Result<List<AssessPoint>> SeAssesss = seAssessService.getreplyAssessList(sysUser, pageInfo);
        return ResponseEntity.ok(SeAssesss);
    }

    @GetMapping("/{assessId}/detail")
    @ApiOperation(value = "获取评价详细信息")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getAssessDetail(@PathVariable String assessId) {
        Result<AssessPoint> orderCenter = seAssessService.getAssessDetail(assessId);
        return ResponseEntity.ok(orderCenter);
    }

    @PostMapping
    @ApiOperation(value = "回复评价")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity addAssess(@RequestBody SeAssessContent seAssessContent,@AuthenticationPrincipal SysUser sysUser) {
        Result<List<SeAssessContent>> orderCenter = seAssessService.addAssess(seAssessContent,sysUser);
        return ResponseEntity.ok(orderCenter);
    }

    @PostMapping("/wx")
    @ApiOperation(value = "微信端添加评价")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity addAssessByWx(@RequestBody SeAssessByWx seAssessByWx, @AuthenticationPrincipal SysUser sysUser) {
        Result result = seAssessService.addAssessByWx(seAssessByWx,sysUser);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/wx/allAssess")
    @ApiOperation(value = "获取评价")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity getAllAssess( PageInfo pageInfo,@AuthenticationPrincipal SysUser sysUser) {
        Result result = seAssessService.getAllAssess(pageInfo,sysUser);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    @ApiOperation(value = "删除评价")
    @ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
    public ResponseEntity deleteAssess(@RequestParam String ids) {
        Result<String> result = seAssessService.deleteAssess(ids);
        return ResponseEntity.ok(result);
    }

}