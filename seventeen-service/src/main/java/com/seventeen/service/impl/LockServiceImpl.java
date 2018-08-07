package com.seventeen.service.impl;

import com.alibaba.fastjson.JSON;
import com.seventeen.bean.HttpBean.ResLockToken;
import com.seventeen.bean.SeApartment;
import com.seventeen.common.utils.HttpUtil;
import com.seventeen.mapper.SeApartmentMapper;
import com.seventeen.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 锁相关服务类
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    SeApartmentMapper seApartmentMapper;

    private static String client_id = "2811b27c42904095b606af45376449a0";
    private static String client_secret = "bd192ace33112301a2e5f1c3f3306c0b";
    private static String grant_type = "password";
    private static String username = "2126539855@qq.com";
    private static String password = "e10adc3949ba59abbe56e057f20f883e";
    private static String redirect_uri = "https://www.17Inn.com";


    private static String token = "";

    /**
     * 构造获取 token
     */
    public  LockServiceImpl() {
        String data="client_id="+client_id +"&client_secret="+client_secret +"&grant_type=password" +"&username="+username  +"&password="+password +"&redirect_uri="+redirect_uri;
        String s = HttpUtil.postData("https://api.sciener.cn/oauth2/token", data,"application/x-www-form-urlencoded");
        ResLockToken tokenObj = JSON.parseObject(s, ResLockToken.class);
        token = tokenObj.getAccess_token();
    }


    @Override
    public void updataLockPassWord(String apId, LocalDateTime startTime, LocalDateTime endTime,int pwd) {
        SeApartment seApartment=new SeApartment();
        seApartment.setId(apId);
        seApartment= seApartmentMapper.selectOne(seApartment);


        long start = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long end = endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        Map<String,Object> map =new HashMap<>();
        map.put("clientId",client_id);
        map.put("accessToken",token);
        map.put("lockId",seApartment.getLockId());
        map.put("keyboardPwdId",seApartment.getLockPwdId());
        map.put("newKeyboardPwd",pwd);
        map.put("startDate",start);
        map.put("endDate",end);
        map.put("changeType","2");
        map.put("date",System.currentTimeMillis());



        String s = HttpUtil.postData("https://api.sciener.cn/v3/keyboardPwd/change", mapToDataString(map),"application/x-www-form-urlencoded");
        System.out.println(s);

    }

    public static void main(String[] args) {
//        LockServiceImpl s=new LockServiceImpl();
//        LocalDateTime.now();
//        s.updataLockPassWord("20180725230836716205288",LocalDateTime.now(),LocalDateTime.now().plusDays(1));
        System.out.println(System.currentTimeMillis());
    }

    String mapToDataString(Map<String,Object> map){
        StringBuffer sb=new StringBuffer();
        List<String> keyList= new ArrayList<>(map.keySet());
        for (int i = 0; i < keyList.size(); i++) {
            if(i>0)
                sb.append("&");
            sb.append(keyList.get(i));
            sb.append("=");
            sb.append(map.get(keyList.get(i)));
        }
        return sb.toString();
    }

}
