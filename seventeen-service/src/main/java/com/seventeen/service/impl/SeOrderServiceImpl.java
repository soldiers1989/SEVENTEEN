package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
public class SeOrderServiceImpl  implements SeOrderService  {
    private final Logger logger = LoggerFactory.getLogger(SeOrderServiceImpl.class);

    @Autowired
    private SeOrderMapper seOrderMapper;

    /**
     * @param status
     * @param remark
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result<List<OrderCenter>> getOrderList(SysUser sysUser,String status, String remark, PageInfo pageInfo, String startTime, String endTime) {
        Result<List<OrderCenter>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);

            String admin = "";
            /**
             * 小程序用户
             */
            if(sysUser.getRoleIds().contains("admin")){
                admin =  sysUser.getId();
            }

            ArrayList<OrderCenter> orderCenters = seOrderMapper.getSeOrders(admin,status, remark,startTime,endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> getOrderDetail(String orderId) {

        Result<OrderCenter> result = new Result<>();
        try {
            OrderCenter orderCenter = seOrderMapper.getOrderDetail(orderId);
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<OrderCenter>> noReplyOrderList(String reply,SysUser sysUser, PageInfo pageInfo) {

        Result<List<OrderCenter>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            String id = sysUser.getId();
            ArrayList<OrderCenter> orderCenters = seOrderMapper.getNoReplyOrderList(id,reply);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> noReplyOrder(String orderId, SysUser sysUser) {
        Result<OrderCenter> result = new Result<>();
        try {

            OrderCenter orderCenter = seOrderMapper.noReplyOrder(orderId,"0");
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

}
