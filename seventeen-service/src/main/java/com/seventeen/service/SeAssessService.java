package com.seventeen.service;


import com.seventeen.bean.AssessPoint;
import com.seventeen.bean.SeAssessByWx;
import com.seventeen.bean.SeAssessContent;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */
public interface SeAssessService {


    /**
     * @param isCheck
     * @param remark
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    Result<List<AssessPoint>> getAssessList(String isCheck, String remark, PageInfo pageInfo, String startTime, String endTime);

    /**
     * @param assessId
     * @return
     */
    Result<AssessPoint> getAssessDetail(String assessId);

    /**
     * @param seAssessContent
     * @return
     */
    Result<List<SeAssessContent>> addAssess(SeAssessContent seAssessContent);

    /**
     *
     * @param ids
     * @return
     */
    Result<String> deleteAssess(String ids);

    /**
     *
     * @param sysUser
     * @param pageInfo
     * @return
     */
    Result<List<AssessPoint>> getreplyAssessList(SysUser sysUser, PageInfo pageInfo);

    Result addAssessByWx(SeAssessByWx seAssessByWx, SysUser sysUser);

}
