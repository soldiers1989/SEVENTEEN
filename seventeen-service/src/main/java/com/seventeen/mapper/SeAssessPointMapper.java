package com.seventeen.mapper;

import com.seventeen.bean.SeAssessPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAssessPointMapper extends CoreMapper<SeAssessPoint> {

    @Select("select st.`name`,sa.point from se_assess_point sa  LEFT JOIN se_tag st on sa.tag_id = st.id where sa.id=#{assessId}")
    List<SeAssessPoint> getseAssessPoint(@Param("assessId") String assessId);

}