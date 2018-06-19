package com.seventeen.controller;


import com.seventeen.util.PageInfo;
import com.seventeen.bean.core.SysAuthority;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.service.impl.SysAuthorityService;
import com.seventeen.service.impl.SysUserService;
import com.seventeen.util.IDGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sys/authorities")
@Api(tags = "权限管理")
public class SysAuthorityController {
	@Autowired
	private SysAuthorityService sysAuthorityService;
	@Autowired
	private SysUserService sysUserService;

	@PostMapping
	@ApiOperation("新增权限")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@CacheEvict(value = "sysAuthorityList", allEntries = true)
	public ResponseEntity add(@RequestBody SysAuthority sysAuthority) {
		sysAuthority.setId(IDGenerator.getId());
		sysAuthority.setCreateDate(new Date());
		sysAuthorityService.insert(sysAuthority);
		return ResponseEntity.ok("保存成功。");
	}

	@DeleteMapping("/{authorityId}")
	@ApiOperation("根据权限id删除权限")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer "),
			@ApiImplicitParam(name = "authorityId", value = "权限id", paramType = "path", required = true) })
	@Caching(evict = { @CacheEvict(value = "sysAuthority", key = "#authorityId"),
			@CacheEvict(value = "sysAuthorityList", allEntries = true),
			@CacheEvict(value = "sysUserDetails", allEntries = true) })
	public ResponseEntity delete(@PathVariable String authorityId) {
		int count = sysAuthorityService.deleteById(authorityId);
		return ResponseEntity.ok("删除成功" + count + "条数据。");
	}

	@DeleteMapping
	@ApiOperation("根据权限id列表删除权限")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer "),
			@ApiImplicitParam(name = "authorityIds", value = "权限id列表,格式为1,2,3", paramType = "query", required = true) })
	@Caching(evict = { @CacheEvict(value = "sysAuthority", allEntries = true),
			@CacheEvict(value = "sysAuthorityList", allEntries = true),
			@CacheEvict(value = "sysUserDetails", allEntries = true) })
	public ResponseEntity deleteList(@RequestParam String authorityIds) {
		int count = sysAuthorityService.deleteByIds(authorityIds);
		return ResponseEntity.ok("删除成功" + count + "条数据。");
	}

	@PutMapping("/{authorityId}")
	@ApiOperation("根据权限id修改权限信息。")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer "),
			@ApiImplicitParam(name = "authorityId", value = "权限id", paramType = "path", required = true) })
	@Caching(evict = { @CacheEvict(value = "sysAuthority", key = "#authorityId"),
			@CacheEvict(value = "sysAuthorityList", allEntries = true),
			@CacheEvict(value = "sysUserDetails", allEntries = true) })
	public ResponseEntity update(@RequestBody SysAuthority sysAuthority, @PathVariable String authorityId) {
		sysAuthority.setId(authorityId);
		sysAuthority.setModifyDate(new Date());
		sysAuthorityService.update(sysAuthority);
		return ResponseEntity.ok("更新成功。");
	}

	@GetMapping("/all")
	@ApiOperation("查询所有权限列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthorityList")
	public ResponseEntity findList() {
		return ResponseEntity.ok(sysAuthorityService.findAll());
	}

	@GetMapping
	@ApiOperation("分页查询权限列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthorityList")
	public ResponseEntity findList(PageInfo pageInfo) {
		return ResponseEntity.ok(new Result(sysAuthorityService.findAll(pageInfo), pageInfo));
	}

	@GetMapping("{authorityId}")
	@ApiOperation("根据权限id查询权限信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthority", key = "#authorityId")
	public ResponseEntity findOne(@PathVariable String authorityId) {
		return ResponseEntity.ok(sysAuthorityService.findById(authorityId));
	}

	@GetMapping("/user/current")
	@ApiOperation("根据当前登录用户查询权限列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthorityList", key = "'sysAuthorityList_' + #userDetails.id")
	public ResponseEntity findListByUserCurrent(@AuthenticationPrincipal UserDetails userDetails) {
		String username = userDetails.getUsername();
		SysUser sysUser = sysUserService.findBy("username", username);
		return ResponseEntity.ok(sysAuthorityService.findByUserId(sysUser.getId()));
	}

	@GetMapping("/user/{userId}")
	@ApiOperation("根据用户id查询权限列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthorityList", key = "'sysAuthorityList_' + #userId")
	public ResponseEntity findListByUserId(@PathVariable String userId) {
		return ResponseEntity.ok(sysAuthorityService.findByUserId(userId));
	}

	@GetMapping("/role/{roleId}")
	@ApiOperation("根据角色id查询权限列表")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	@Cacheable(value = "sysAuthorityList", key = "'sysAuthorityList_' + #roleId")
	public ResponseEntity findListByRoleId(@PathVariable String roleId) {
		return ResponseEntity.ok(sysAuthorityService.findByRoleId(roleId));
	}

	@GetMapping("clear")
	@ApiOperation("清除所有权限缓存")
	@Caching(evict = { @CacheEvict(value = "sysAuthority", allEntries = true),
			@CacheEvict(value = "sysAuthorityList", allEntries = true) })
	public ResponseEntity clearCache() {
		return ResponseEntity.ok("权限缓存清除成功。");
	}
}
