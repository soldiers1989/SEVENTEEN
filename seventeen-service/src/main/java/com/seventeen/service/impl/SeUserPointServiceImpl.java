package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.SeShop;
import com.seventeen.bean.SeUserPoint;
import com.seventeen.bean.SeUserPointLog;
import com.seventeen.bean.UserPoint;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeUserPointLogMapper;
import com.seventeen.mapper.SeUserPointMapper;
import com.seventeen.service.SeUserPointService;
import com.seventeen.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
@Slf4j
public class SeUserPointServiceImpl implements SeUserPointService {
    @Autowired
    private SeUserPointMapper seUserPointMapper;
    @Autowired
    private SeUserPointLogMapper seUserPointLogMapper;
    @Override
    public Result<List<UserPoint>> getSeUserPointList(String remark, PageInfo pageInfo) {

        Result<List<UserPoint>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            List<UserPoint> userPoints = seUserPointMapper.getSeUserPointList(remark);
            pageInfo.setTotal(page.getTotal());
            result.setData(userPoints, pageInfo);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<UserPoint> getSeUserPointDetail(String id) {
        Result<UserPoint> result = new Result<>();
        try {
            UserPoint userPoints = seUserPointMapper.getSeUserPoint(id);
            SeUserPointLog seUserPointLog = new SeUserPointLog();
            seUserPointLog.setUserId(userPoints.getUserId());
            List<SeUserPointLog> seUserPointLogs = seUserPointLogMapper.select(seUserPointLog);
            userPoints.setSeUserPointLogs(seUserPointLogs);
            result.setData(userPoints);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }
}
