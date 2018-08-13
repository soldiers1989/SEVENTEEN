package com.seventeen.mapper;

import com.seventeen.bean.SeApartmentGood;
import com.seventeen.bean.SeTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeApartmentGoodMapper extends CoreMapper<SeApartmentGood>{

    @Select("select * from se_apartment_good where ap_id = #{id}")
    List<SeApartmentGood> selectByApids(@Param("id") String apNum);

    /**
     *
     * @param id
     */
    @Delete("DELETE FROM se_apartment_good WHERE ap_id = #{id}")
    void deleteByApid(@Param("id") String id);

    @Select("SELECT\n" +
            "\tst.`name`,\n" +
            "\tst.`value`\n" +
            "FROM\n" +
            "\tse_apartment_good sag,\n" +
            "\tse_tag st,\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\tid\n" +
            "\t\tFROM\n" +
            "\t\t\tse_apartment sa\n" +
            "\t\tWHERE\n" +
            "\t\t\tsa.room_type = #{roomTyype}\n" +
            "\t\tORDER BY\n" +
            "\t\t\tsa.create_time DESC\n" +
            "\t\tLIMIT 1\n" +
            "\t) sa\n" +
            "WHERE\n" +
            "\tsa.id = sag.ap_id\n" +
            "AND sag.tag_id = st.id\n" +
            "AND st.`STATUS` = '1'")
    ArrayList<SeTag> getGoods(@Param("roomType") String roomType);

}