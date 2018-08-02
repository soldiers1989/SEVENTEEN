package com.seventeen.mapper;

import com.seventeen.bean.SeAssessPoint;
import com.seventeen.bean.WxTotalAssess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAssessPointMapper extends CoreMapper<SeAssessPoint> {

    @Select("select st.`name`,sa.point from se_assess_point sa  LEFT JOIN se_tag st on sa.tag_id = st.id where sa.id=#{assessId}")
    List<SeAssessPoint> getseAssessPoint(@Param("assessId") String assessId);

    @Select("SELECT ROUND(AVG(sao.point) +0.0000001) totalPoint FROM se_assess_point sao")
    String getseAssessTotalPoint();

    @Select("SELECT\n" +
            "\tst. NAME target,\n" +
            "\tROUND(AVG(sao.point)+0.0000001)  point\n" +
            "FROM\n" +
            "\tse_assess_point sao,\n" +
            "\tse_tag st\n" +
            "WHERE\n" +
            "\tsao.tag_id = st.id\n" +
            "GROUP BY\n" +
            "\tsao.tag_id")
    List<WxTotalAssess.AssessTarget> getseAssessList();
}