package com.seventeen.service;

import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */
public interface SeCouponService {

    /**
     * @param statusk
     * @param remark
     * @param pageInfo
     * @return
     */
    Result<List<SeCoupon>> getCouponList(String status, String remark, PageInfo pageInfo);

    /**
     * @param seCoupon
     * @return
     */
    Result<SeCoupon> addCoupon(SeCoupon seCoupon);

    /**
     * @param ids
     * @return
     */
    Result<String> deleteCoupon(String ids);


    /**
     * @param couponId
     * @return
     */
    Result<SeCoupon> getCouponDetail(String couponId);


    /**
     *
     * @param seCoupon
     * @return
     */
    Result<SeCoupon> updateCoupon(SeCoupon seCoupon);


    /**
     *
     */
    void updateCouponStatus();

    /**
     *
     * @param status
     * @param remark
     * @param startTime
     * @param endTime
     * @param pageInfo
     * @return
     */
    Result<List<CouponLog>> getCouponLog(String status, String remark, String startTime, String endTime, PageInfo pageInfo);

    Result<List<SeCoupon>> couponListWx(String status, PageInfo pageInfo, SysUser sysUser);

    /**
     *
     * @param id
     * @param sysUser
     * @return
     */
    Result<String> exchangeCoupon(String id, SysUser sysUser);

    Result<Map> getCouponByRoomType(String roomType);

    Result<List<SeCoupon>> getCouponByOrderCanUse(SysUser sysUser, String roomType, String startTime, String endTime);

    Result<Map> getCanUseByPriceType(String priceType);

}
