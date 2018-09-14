package com.seventeen.mapper;


import com.seventeen.bean.SeUserPoint;
import com.seventeen.bean.UserPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeUserPointMapper extends CoreMapper<SeUserPoint>{

    @Select("<script> SELECT\n" +
            "\tsup.id,\n" +
            "\tsu.username userName,\n" +
            "\tsup.point,\n" +
            "\tsup.update_time updateTime\n" +
            "FROM\n" +
            "\tse_user_point sup,\n" +
            "\tsys_user su\n" +
            "WHERE\n" +
            "\t1 = 1\n" +
            "AND su.id = sup.user_id " +
            " <if test=\"remark != null and remark  != ''\">" +
            "     AND ( su.`username` like CONCAT('%',#{remark},'%' ) )" +
            " </if>" +
            "</script>")
    List<UserPoint> getSeUserPointList(@Param("remark") String remark);

    @Select("<script> SELECT\n" +
            "\tsup.id,\n" +
            "\tsu.username userName,\n" +
            "\tsu.id userId,\n" +
            "\tsup.point,\n" +
            "\tsup.update_time updateTime\n" +
            "FROM\n" +
            "\tse_user_point sup,\n" +
            "\tsys_user su\n" +
            "WHERE\n" +
            "\t1 = 1\n" +
            "AND su.id = sup.user_id " +
            "AND sup.id = #{id}" +
            "</script>")
    UserPoint getSeUserPoint(@Param("id") String id);

    @Select("<script> SELECT\n" +
            "\tsup.id,\n" +
            "\tsu.username userName,\n" +
            "\tsu.id userId,\n" +
            "\tsup.point,\n" +
            "\tsup.update_time updateTime\n" +
            "FROM\n" +
            "\tse_user_point sup,\n" +
            "\tsys_user su\n" +
            "WHERE\n" +
            "\t1 = 1\n" +
            "AND su.id = sup.user_id " +
            "AND sup.user_id = #{userId}" +
            "</script>")
    UserPoint getSeUserPoint1(String userId);
}