package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import com.seventeen.bean.SeCouponPriceType;
import com.seventeen.bean.SeUserCoupon;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeCouponMapper;
import com.seventeen.mapper.SeCouponPriceTypeMapper;
import com.seventeen.mapper.SeUserCouponMapper;
import com.seventeen.service.SeCouponService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
public class SeCouponServiceImpl implements SeCouponService {
    private final Logger logger = LoggerFactory.getLogger(SeCouponServiceImpl.class);

    @Autowired
    private SeCouponMapper SeCouponMapper;

    @Autowired
    private SeCouponPriceTypeMapper seCouponPriceTypeMapper;

    @Autowired
    private SeUserCouponMapper seUserCouponMapper;


    @Override
    public Result<List<SeCoupon>> getCouponList(String status, String remark, PageInfo pageInfo) {
        Result<List<SeCoupon>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<SeCoupon> seCoupons = SeCouponMapper.getSeCoupons(status, remark);
            pageInfo.setTotal(page.getTotal());
            result.setData(seCoupons, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<SeCoupon> addCoupon(SeCoupon seCoupon) {
        Result<SeCoupon> result = new Result<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            seCoupon.setId(IDGenerator.getId());
            seCoupon.setCreateBy(authentication.getName());
            seCoupon.setStatus("1");
            seCoupon.setCreateTime(DateUtil.now());
            seCoupon.setSendType(String.join(",", seCoupon.getSendTypeArr()));
            List<String> priceTypes = seCoupon.getPriceTypes();
            for (String priceType : priceTypes) {
                SeCouponPriceType seCouponPriceType = new SeCouponPriceType();
                seCouponPriceType.setTagId(priceType);
                seCouponPriceType.setCouponId(seCoupon.getId());
                seCouponPriceType.setCreateTime(DateUtil.now());
                seCouponPriceType.setCreateBy(authentication.getName());
                seCouponPriceTypeMapper.insert(seCouponPriceType);
            }
            SeCouponMapper.insert(seCoupon);
            result.setData(seCoupon);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteCoupon(String ids) {
        Result<String> result = new Result<>();
        try {
            String[] split = ids.split(",");
            SeCouponMapper.deleteCoupon(split);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<SeCoupon> getCouponDetail(String couponId) {
        Result<SeCoupon> result = new Result<>();
        try {
            SeCoupon seCoupon = new SeCoupon();
            SeCouponPriceType seCouponPriceType = new SeCouponPriceType();

            seCoupon.setId(couponId);
            seCouponPriceType.setCouponId(couponId);
            List<SeCoupon> seCoupons = SeCouponMapper.select(seCoupon);
            List<SeCouponPriceType> seCouponPriceTypes = seCouponPriceTypeMapper.select(seCouponPriceType);


            seCoupon = seCoupons.get(0);
            List<String> vals = Arrays.asList(seCoupon.getSendType().split(","));
            seCoupon.setSendTypeArr(vals);
            seCoupon.setPriceTypes(seCouponPriceTypes.stream().map(SeCouponPriceType::getTagId).collect(Collectors.toList()));
            result.setData(seCoupon);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<SeCoupon> updateCoupon(SeCoupon seCoupon) {
        Result<SeCoupon> result = new Result<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LocalDate startTime = DateUtil.parseDate(seCoupon.getStartTime(), "yyyy-MM-dd");
            LocalDate endTime = DateUtil.parseDate(seCoupon.getEndTime(), "yyyy-MM-dd");
            boolean flag = DateUtil.betweenDate(startTime, endTime);
            if (flag) {
                seCoupon.setStatus("1");
            } else {
                seCoupon.setStatus("2");
            }
            seCoupon.setSendType(String.join(",", seCoupon.getSendTypeArr()));
            seCoupon.setCreateBy(authentication.getName());
            seCoupon.setCreateTime(DateUtil.now());
            List<String> priceTypes = seCoupon.getPriceTypes();
            SeCouponPriceType seCouponPriceTypeDe = new SeCouponPriceType();
            seCouponPriceTypeDe.setCouponId(seCoupon.getId());
            seCouponPriceTypeMapper.delete(seCouponPriceTypeDe);
            for (String priceType : priceTypes) {
                SeCouponPriceType seCouponPriceType = new SeCouponPriceType();
                seCouponPriceType.setTagId(priceType);
                seCouponPriceType.setCouponId(seCoupon.getId());
                seCouponPriceType.setCreateTime(DateUtil.now());
                seCouponPriceType.setCreateBy(authentication.getName());
                seCouponPriceTypeMapper.insert(seCouponPriceType);
            }

            SeCouponMapper.updateByPrimaryKeySelective(seCoupon);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCouponStatus() {
        Result<SeCoupon> result = new Result<>();
        try {
            String date = DateUtil.formatDate(LocalDate.now());
            List<SeCoupon> seCoupons = SeCouponMapper.selectCouponEveryDay(date);
            if (!seCoupons.isEmpty()) {
                for (SeCoupon seCoupon : seCoupons) {
                    seCoupon.setStatus("2");
                    SeCouponMapper.updateByPrimaryKey(seCoupon);
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Result<List<CouponLog>> getCouponLog(String status, String remark, String startTime, String endTime, PageInfo pageInfo) {
        Result<List<CouponLog>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<CouponLog> couponLogs = SeCouponMapper.getCouponLog(status, remark, startTime, endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(couponLogs, pageInfo);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<SeCoupon>> couponListWx(String status, PageInfo pageInfo, SysUser sysUser) {
        Result<List<SeCoupon>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<SeCoupon> seCoupons = SeCouponMapper.couponListWx(status, sysUser.getId());
            pageInfo.setTotal(page.getTotal());
            result.setData(seCoupons, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> exchangeCoupon(String id, SysUser sysUser) {
        Result<String> result = new Result<>();
        try {
            SeCoupon seCoupon = new SeCoupon().setId(id).setStatus("1");

            List<SeCoupon> seCoupons = SeCouponMapper.select(seCoupon);
            if (!seCoupons.isEmpty()) {
                SeUserCoupon seUserCoupon = new SeUserCoupon();
                seUserCoupon.setCouponId(id);
                seUserCoupon.setUserId(sysUser.getId());
                List<SeUserCoupon> seUserCoupons = seUserCouponMapper.select(seUserCoupon);
                if (seUserCoupons.isEmpty()) {
                    seUserCoupon.setId(IDGenerator.getId());
                    seUserCoupon.setUpdateTime(DateUtil.now());
                    seUserCoupon.setCreateBy(sysUser.getId());
                    seUserCoupon.setCreateTime(DateUtil.now());
                    seUserCoupon.setStatus("1");
                    seUserCouponMapper.insert(seUserCoupon);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<SeCoupon>> getCouponByRoomType(String roomType) {
        Result<List<SeCoupon>> result = new Result<>();
        try {

            int count = SeCouponMapper.getCouponByRoomType(roomType);
            if (count == 0) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "没有可用优惠券");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<SeCoupon>> getCouponByOrderCanUse(SysUser sysUser, String roomType, String startTime, String endTime) {
        Result<List<SeCoupon>> result = new Result<>();
        try {

            int count = SeCouponMapper.getCouponByRoomType(roomType);
            if (count == 0) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "没有可用优惠券");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

}
