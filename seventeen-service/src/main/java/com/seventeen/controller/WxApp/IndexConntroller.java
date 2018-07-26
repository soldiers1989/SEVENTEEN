package com.seventeen.controller.WxApp;

import com.seventeen.bean.RoomTypePirce;
import com.seventeen.core.Result;
import com.seventeen.service.SeApartmentService;
import com.seventeen.service.SeShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

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

    /**
     * 获取类型价格
     * @param typeCode
     * @return
     */
    @GetMapping("/getRoomPirce")
    @ResponseBody
    public ResponseEntity getRoomPirce(String typeCode) {
        Result<List<RoomTypePirce>> typePiece = seApartmentService.getTypePiece(typeCode);
        return ResponseEntity.ok(typePiece);
    }


    public static void main(String[] args) {
        long l = LocalDateTime.of(LocalDate.now().plusDays(3), LocalTime.now()).toInstant(ZoneOffset.of("+8")).toEpochMilli();

        System.out.println(l);

        System.out.println(System.currentTimeMillis());
    }

}
