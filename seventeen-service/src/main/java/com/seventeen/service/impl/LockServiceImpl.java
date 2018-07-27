package com.seventeen.service.impl;

import com.seventeen.service.LockService;
import org.springframework.stereotype.Service;

/**
 * 锁相关服务类
 */
@Service
public class LockServiceImpl implements LockService{
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

    private  static String token="";

    /**
     * 构造获取 token
     */
    public void  LockServiceImpl(){
        token="";
    }

}
