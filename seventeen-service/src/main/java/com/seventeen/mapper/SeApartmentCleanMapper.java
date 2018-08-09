package com.seventeen.mapper;

import com.seventeen.bean.ApartmentClean;
import com.seventeen.bean.SeApartmentClean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface SeApartmentCleanMapper extends CoreMapper<SeApartmentClean> {

    @Select("<script>SELECT\n" +
            "\tss.name shopName,\n" +
            "  sa.ap_num apNum,\n" +
            "  sa.id apId,\n" +
            "\tsac.create_time createTime,\n" +
            "\tif(sac.is_cleaned='1','是','未') isCleaned\n" +
            "FROM\n" +
            "\tse_apartment_clean sac,\n" +
            "\tse_apartment sa,\n" +
            "\tse_shop ss\n" +
            "WHERE\n" +
            "\tsac.ap_id = sa.id\n" +
            "AND sa.shop_id = ss.id\n" +
            " <if test=\"apNum != null and apNum  != ''\">" +
            "     AND sa.ap_num = #{apNum} " +
            " </if>\n" +
            "ORDER BY\n" +
            "\tCASE\n" +
            "WHEN sac.is_cleaned = '1' THEN\n" +
            "\t1\n" +
            "end asc\n" +
            ",\n" +
            "sac.create_time DESC" +
            "</script>")
    ArrayList<ApartmentClean> getClean(@Param("apNum") String apNum);

}