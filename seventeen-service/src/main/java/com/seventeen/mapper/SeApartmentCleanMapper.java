package com.seventeen.mapper;

import com.seventeen.bean.ApartmentClean;
import com.seventeen.bean.SeApartmentClean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
            "AND sac.is_cleaned = '0'\n" +
            "ORDER BY\n" +
            "\tCASE\n" +
            "WHEN sac.is_cleaned = '1' THEN\n" +
            "\t1\n" +
            "end asc\n" +
            ",\n" +
            "sac.create_time DESC" +
            "</script>")
    ArrayList<ApartmentClean> getCleanToday();

    @Update("update se_apartment_clean sac set sac.is_cleaned='1'")
    void upgradeCleanRoom();

    @Select("SELECT\n" +
            "\tsai.mix_url\n" +
            "FROM\n" +
            "\tse_apartment_img sai,\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tid\n" +
            "\t\tFROM\n" +
            "\t\t\tse_apartment sa\n" +
            "\t\twhere  sa.room_type = #{roomType}\n" +
            "\t\tORDER BY\n" +
            "\t\t\tsa.create_time DESC\n" +
            "\t\tLIMIT 1\n" +
            "\t) sa\n" +
            "WHERE\n" +
            "\tsa.id = sai.ap_id\n" +
            "ORDER BY\n" +
            "\tsai.create_time DESC\n" +
            "limit 10")
    ArrayList<String> getAapartmentImgs(@Param("roomType") String roomType);

}