package com.seventeen.controller;


import com.seventeen.bean.SeShop;
import com.seventeen.core.Result;
import com.seventeen.service.SeShopService;
import com.seventeen.util.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@Api(tags = "门店")
@Slf4j
public class ShopController {


	@Autowired
    private SeShopService SeShopService;

	@GetMapping
	@ApiOperation(value = "获取门店列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity ShopList(String remark,PageInfo pageInfo) {
        Result<List<SeShop>> SeShops = SeShopService.getShopList(remark,pageInfo);
        return ResponseEntity.ok(SeShops);
	}

	@PostMapping
	@ApiOperation(value = "添加门店列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity addShop(@RequestBody SeShop seShop) {
		Result SeShops = SeShopService.addShop(seShop);
		return ResponseEntity.ok(SeShops);
	}

	@PostMapping("/update")
	@ApiOperation(value = "更新门店列表信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity updateShop(@RequestBody SeShop seShop) {
		Result SeShops = SeShopService.updateShop(seShop);
		return ResponseEntity.ok(SeShops);
	}

	@GetMapping("/{shopId}/detail")
	@ApiOperation(value = "获取门店详细信息")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity getShopDetail(@PathVariable String shopId) {
		Result<SeShop> ShopCenter = SeShopService.getShopDetail(shopId);
		return ResponseEntity.ok(ShopCenter);
	}

	@DeleteMapping
	@ApiOperation(value = "获取门店")
	@ApiImplicitParam(name = "Authorization", value = "Bearer token", paramType = "header", required = true, defaultValue = "Bearer ")
	public ResponseEntity deleteShop(@RequestParam String ids) {
		Result seApartments = SeShopService.deleteShop(ids);
		return ResponseEntity.ok(seApartments);
	}

}