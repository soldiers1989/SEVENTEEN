package com.seventeen.service;

import com.seventeen.bean.UserPoint;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */

public interface SeUserPointService {


    /**
     *
     * @param remark
     * @param pageInfo
     * @return
     */
    Result<List<UserPoint>> getSeUserPointList(String remark, PageInfo pageInfo);

    /**
     *
     * @param id
     * @return
     */
    Result<UserPoint> getSeUserPointDetail(String id);

}
