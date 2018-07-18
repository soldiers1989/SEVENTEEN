package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.*;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeAssessContentMapper;
import com.seventeen.mapper.SeAssessMapper;
import com.seventeen.mapper.SeAssessPointMapper;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.service.SeAssessService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
@Slf4j
public class SeAssessServiceImpl implements SeAssessService {

    @Autowired
    private SeAssessMapper seAssessMapper;

    @Autowired
    private SeAssessPointMapper seAssessPointMapper;

    @Autowired
    private SeAssessContentMapper seAssessContentMapper;

    @Autowired
    private SeOrderMapper seOrderMapper;

    @Override
    public Result<List<AssessPoint>> getAssessList(String isCheck, String remark, PageInfo pageInfo, String startTime, String endTime) {
        Result<List<AssessPoint>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);

            ArrayList<AssessPoint> assessPoints = seAssessMapper.getAssessList(isCheck, remark, startTime, endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(assessPoints, pageInfo);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<AssessPoint> getAssessDetail(String assessId) {
        Result<AssessPoint> result = new Result<>();
        try {
            SeAssessPoint seAssessPoint = new SeAssessPoint();
            seAssessPoint.setId(assessId);
            AssessPoint assessPoints = seAssessMapper.getAssessDetail(assessId);
            List<SeAssessPoint> seAssessPoints = seAssessPointMapper.getseAssessPoint(assessId);
            List<SeAssessContent> seAssessContents = seAssessContentMapper.getseAssessContent(assessPoints.getContentId());
            assessPoints.setSeAssessPoints(seAssessPoints);
            assessPoints.setSeAssessContents(seAssessContents);
            result.setData(assessPoints);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<List<SeAssessContent>> addAssess(SeAssessContent seAssessContent) {
        Result<List<SeAssessContent>> result = new Result<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String id = seAssessContent.getAssessId();
            seAssessContent.setCreateBy(authentication.getName());
            seAssessContent.setCreateTime(DateUtil.now());
            List<SeAssessContent> seAssessContents = seAssessContentMapper.getseAssessContent(id);
            /**
             * 只保存两条记录 不再累加
             */
            if (seAssessContents.size() == 0) {
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "暂无评论，无法回复");
            }
            if (seAssessContents.size() == 1) {
                SeAssess seAssess = new SeAssess();
                seAssess.setId(id);
                seAssess.setIsCheck("1");
                seAssessContent.setId(IDGenerator.getId());
                seAssessContent.setParentId(id);
                seAssessContent.setStatus("1");
                seAssessContentMapper.insert(seAssessContent);
                seAssessMapper.updateByPrimaryKeySelective(seAssess);
                seAssessContents.add(seAssessContent);
                return result.setData(seAssessContents);
            }
            SeAssessContent seAssessContent1 = seAssessContents.get(1);
            seAssessContent1.setContent(seAssessContent.getContent());
            seAssessContentMapper.updateByPrimaryKeySelective(seAssessContent1);
            result.setData(seAssessContents);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteAssess(String ids) {
        Result result = new Result<>();
        try {
            SeAssess seAssess = new SeAssess();
            seAssess.setId(ids);
            seAssess.setStatus("0");
            seAssessMapper.updateByPrimaryKeySelective(seAssess);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<AssessPoint>> getreplyAssessList(SysUser sysUser, PageInfo pageInfo) {
        Result<List<AssessPoint>> result = new Result<>();
        try {

            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<AssessPoint> assessPoints = seAssessMapper.getAssessByUser(sysUser.getId());
            pageInfo.setTotal(page.getTotal());

            for (AssessPoint assessPoint : assessPoints) {
                List<SeAssessPoint> seAssessPoints = seAssessPointMapper.getseAssessPoint(assessPoint.getAssessId());
                List<SeAssessContent> seAssessContents = seAssessContentMapper.getseAssessContent(assessPoint.getContentId());
                assessPoint.setSeAssessPoints(seAssessPoints);
                assessPoint.setSeAssessContents(seAssessContents);
            }

            result.setData(assessPoints, pageInfo);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAssessByWx(SeAssessByWx seAssessByWx, SysUser sysUser) {
        Result result = new Result<>();
        try {
            SeAssess seAssess = new SeAssess();
            String liveSelected = seAssessByWx.getLiveSelected();
            String targetSelected = seAssessByWx.getTargetSelected();
            String orderId = seAssessByWx.getOrderId();
            SeOrder seOrder = new SeOrder();
            seOrder.setId(orderId);
            seOrder = seOrderMapper.selectByPrimaryKey(seOrder);
            seOrder.setIsReply("1");
            seOrderMapper.updateByPrimaryKeySelective(seOrder);

            seAssess.setId(IDGenerator.getId());
            seAssess.setOrderId(orderId);
            seAssess.setUserId(seOrder.getUserId());
            seAssess.setApId(seOrder.getApId());
            seAssess.setIsCheck("0");
            seAssess.setStatus("1");
            seAssess.setBaseTagId(liveSelected);
            if(!"-1".equals(targetSelected)){
                seAssess.setTargetTagId(targetSelected);
            }
            seAssess.setAssessPointId(IDGenerator.getId());
            seAssess.setContentId(IDGenerator.getId());
            seAssess.setCreateBy(sysUser.getUsername());
            seAssess.setCreateTime(DateUtil.now());
            seAssessMapper.insert(seAssess);

            SeAssessContent seAssessContent = new SeAssessContent();
            seAssessContent.setId(IDGenerator.getId());
            seAssessContent.setAssessId(seAssess.getContentId());
            seAssessContent.setContent(seAssessByWx.getContent());
            seAssessContent.setStatus("1");
            seAssessContent.setCreateTime(DateUtil.now());
            seAssessContent.setCreateBy(sysUser.getUsername());
            seAssessContentMapper.insert(seAssessContent);

            List<SeAssessByWx.AssessPoint> points = seAssessByWx.getPoint();
            for (SeAssessByWx.AssessPoint point : points) {
                int totalPoint=0;
                List<String> star = point.getStar();
                for (String s : star) {
                    totalPoint=totalPoint + Integer.valueOf(s);
                }
                SeAssessPoint seAssessPoint = new SeAssessPoint();
                seAssessPoint.setId(seAssess.getAssessPointId());
                seAssessPoint.setTagId(point.getId());
                seAssessPoint.setPoint(String.valueOf(totalPoint));
                seAssessPoint.setPointArr(StringUtils.join(star,","));
                seAssessPoint.setCreateBy(sysUser.getUsername());
                seAssessPoint.setCreateTime(DateUtil.now());
                seAssessPointMapper.insert(seAssessPoint);
            }

        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }
}
