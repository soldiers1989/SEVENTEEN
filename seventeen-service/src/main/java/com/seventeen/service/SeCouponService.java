package com.seventeen.service;

import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */
public interface SeCouponService {

    /**
     * @param status
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

    Result<List<SeCoupon>> couponListWx(String status, PageInfo pageInfo);

}
