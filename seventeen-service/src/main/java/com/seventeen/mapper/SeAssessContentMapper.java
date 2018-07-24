package com.seventeen.mapper;

import com.seventeen.bean.SeAssessContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAssessContentMapper extends CoreMapper<SeAssessContent>{

    @Select("SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tse_assess_content sac\n" +
            "WHERE\n" +
            "\tsac.assess_id =#{assessId} \n" +
            "\t and (parent_id is null or parent_id='')\n" +
            "\n" +
            "UNION\n" +
            "SELECT\n" +
            "\t*\n" +
            "FROM\n" +
            "\tse_assess_content sac\n" +
            "WHERE\n" +
            "\tsac.parent_id = #{assessId}")
    List<SeAssessContent> getseAssessContent(@Param("assessId") String assessId);

}