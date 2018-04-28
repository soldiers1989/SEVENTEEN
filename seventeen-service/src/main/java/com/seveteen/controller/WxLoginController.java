package com.seveteen.controller;

import com.alibaba.fastjson.JSONObject;
import com.seveteen.core.Result;
import com.seveteen.core.ResultGenerator;
import com.seveteen.util.AesCbcUtil;
import com.seveteen.util.HttpRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: csk
 * @Date: 2018/4/12 10:16
 */
@RestController
@RequestMapping("/app/sys")
public class WxLoginController {

    @Value("${wx.app.Appid}")
    //小程序的 app secret (在微信小程序管理后台获取)
    private String  wxspAppid;
    @Value("${wx.app.Secret}")
    //小程序唯一标识   (在微信小程序管理后台获取)
    private String  wxspSecret;
    @Value("${wx.app.grant_type}")
    private String  grant_type;


    @PostMapping("/decodeUserInfo")
    @ResponseBody
    public Result<Map> decodeUserInfo(String encryptedData, String iv, String code) {
        Map map = new HashMap();
        //登录凭证不能为空
        if (code == null || code.length() == 0) {
            return ResultGenerator.genSuccessResult("code 不能为空",-200);
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
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSONObject.parseObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);
                return ResultGenerator.genSuccessResult(new Result(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status", 0);
        map.put("msg", "解密失败");
        return ResultGenerator.genSuccessResult(new Result(map));
    }
}
