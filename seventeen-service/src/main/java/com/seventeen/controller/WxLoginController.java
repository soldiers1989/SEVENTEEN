package com.seventeen.controller;

import com.alibaba.fastjson.JSONObject;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.service.AuthService;
import com.seventeen.util.AesCbcUtil;
import com.seventeen.util.HttpRequest;
import com.seventeen.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: csk
 * @Date: 2018/4/12 10:16
 */
@RestController
@RequestMapping("/app/sys")
public class WxLoginController {

    private final Logger logger = LoggerFactory.getLogger(WxLoginController.class);


    @Value("${wx.app.Appid}")
    //小程序的 app secret (在微信小程序管理后台获取)
    private String wxspAppid;
    @Value("${wx.app.Secret}")
    //小程序唯一标识   (在微信小程序管理后台获取)
    private String wxspSecret;
    @Value("${wx.app.grant_type}")
    private String grant_type;

    @Autowired
    private AuthService authService;


    @PostMapping("/decodeUserInfo")
    @ResponseBody
    @CacheEvict(value = "sysUserList", allEntries = true)
    public ResponseEntity decodeUserInfo(String encryptedData, String iv, String code, String phone) {
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            return ResponseEntity.ok(new Result(-200, "code 不能为空"));
        }
        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
        //请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        //获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        //用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                SysUser sysUser = new SysUser();
                JSONObject userInfoJSON = JSONObject.parseObject(result);
                sysUser.setId(IDGenerator.getId());
                sysUser.setPassword(StringUtils.isEmpty(phone) == true ? "123456" : phone);
                sysUser.setCreateDate(new Date());
                sysUser.setOpenid(userInfoJSON.getString("openId"));
                sysUser.setSex(userInfoJSON.getString("gender"));
                sysUser.setUsername(userInfoJSON.getString("nickName"));
                sysUser.setUnionid(userInfoJSON.getString("unionId"));
                sysUser.setPhone(StringUtils.isEmpty(phone) == true ? "" : phone);
                String token = authService.register(sysUser);
                return ResponseEntity.ok(new Result(token));
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
        return ResponseEntity.ok(new Result(ResultCode.FAIL.getCode(), "解密失败"));
    }
}
