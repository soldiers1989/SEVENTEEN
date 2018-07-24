package com.seventeen.mapper;

import com.seventeen.bean.AssessPoint;
import com.seventeen.bean.SeAssess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface SeAssessMapper extends CoreMapper<SeAssess> {

    ArrayList<AssessPoint> getAssessList(@Param("isCheck") String isCheck, @Param("remark")String remark,@Param("startTime") String startTime, @Param("endTime")String endTime);

    AssessPoint getAssessDetail(@Param("assessId") String assessId);

    ArrayList<AssessPoint> getAssessByUser(@Param("userId") String userId);

}