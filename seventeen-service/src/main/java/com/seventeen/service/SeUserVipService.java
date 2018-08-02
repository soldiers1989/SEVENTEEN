package com.seventeen.service;

import com.seventeen.bean.SeUserVip;
import com.seventeen.core.Result;

public interface SeUserVipService {

    Result<SeUserVip>  getUserVipInfo(String userId);



    /**
     * 升级用户vip等级
     * @param userId
     */
    void addRoomCount(String userId);
}
