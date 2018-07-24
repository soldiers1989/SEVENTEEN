package com.seventeen.controller.WxApp;

import com.seventeen.service.SeApartmentService;
import com.seventeen.service.SeShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序接口
 * lzb- 2018年7月19日10:22:25
 */
@RestController
@Slf4j
@RequestMapping("/app/index")
public class IndexConntroller {
    @Value("${wx.app.Appid}")
    //小程序的 app secret (在微信小程序管理后台获取)
    private String wxspAppid;
    @Value("${wx.app.Secret}")
    //小程序唯一标识   (在微信小程序管理后台获取)
    private String wxspSecret;

    @Autowired
    private SeShopService seShopService;
    @Autowired
    private SeApartmentService seApartmentService;


    @GetMapping("/getShops")
    public ResponseEntity getShops() {
         return ResponseEntity.ok(seShopService.getAllList());
    }

    @GetMapping("/getTypeRooms")
    @ResponseBody
    public ResponseEntity getTypeRooms(String shopId) {
        return ResponseEntity.ok(seApartmentService.getTypeRooms(shopId));
    }


}
