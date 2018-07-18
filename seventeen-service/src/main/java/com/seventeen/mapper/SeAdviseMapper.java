package com.seventeen.mapper;

import com.seventeen.bean.SeAdvise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAdviseMapper extends CoreMapper<SeAdvise> {

    @Select("<script> select sc.* from  se_advise  sc where 1=1 " +
            " <if test=\"startTime != null and startTime  != ''\">" +
            "     AND sc.create_time &gt;= #{startTime}" +
            " </if>" +
            " <if test=\"endTime != null and endTime  != ''\">" +
            "     AND sc.create_time   &lt;= #{endTime} " +
            " </if>" +
            "</script>")
    List<SeAdvise> getSeAdviseList(@Param("startTime")String startTime, @Param("endTime")String endTime);

}