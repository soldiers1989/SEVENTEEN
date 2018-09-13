package com.seventeen.service;


import com.seventeen.bean.AddLiver;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.OrderInfo;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;
import org.springframework.http.ResponseEntity;

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
    Result<List<OrderCenter>> getOrderList(SysUser sysUser,String status, String remark, PageInfo pageInfo, String startTime, String endTime);

    /**
     *
     * @param orderId
     * @return
     */
    Result<OrderCenter> getOrderDetail(String orderId);

    /**
     *
     * @param sysUser
     * @param pageInfo
     * @return
     */
    Result<List<OrderCenter>> noReplyOrderList(String reply,SysUser sysUser, PageInfo pageInfo);

    /**
     *
     * @param orderId
     * @param sysUser
     * @return
     */
    Result<OrderCenter> noReplyOrder(String orderId, SysUser sysUser);

    ResponseEntity setOrder(SysUser sysUser, OrderInfo orderInfo);
    /**
     *
     * @param id
     * @param sysUser
     * @return
     */
    Result<String> deleteOrderByWx(String id, SysUser sysUser);

    /**
     * 支付后修改订单状态
     * @param orderId
     */
    void  updateOrderStatus(String orderId);

    void addOrderCalendar();

    Result getOrderDate(String roomType, SysUser sysUser);

    Result cancelOrder(String order);

    Result cancelOrderConfirm(String order);

    Result updateLockPWD(String orderId, SysUser sysUser);

    void checkOut(String date);

    void upgradeCleanRoom();

    Result addLiver(SysUser sysUser,AddLiver addLiver);

    Result<String> getWifi(SysUser sysUser, String orderId);

}
