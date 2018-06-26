package com.seventeen.service;


import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */
public interface SeOrderService {

    /**
     * @param status
     * @param remark
     * @param pageInfo
     * @return
     */
    Result<List<OrderCenter>> getOrderList(String status, String remark, PageInfo pageInfo, String startTime, String endTime);

    /**
     *
     * @param orderId
     * @return
     */
    Result<OrderCenter> getOrderDetail(String orderId);

}
