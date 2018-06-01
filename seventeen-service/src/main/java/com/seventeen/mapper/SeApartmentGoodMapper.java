package com.seventeen.mapper;

import com.seventeen.bean.SeApartmentGood;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}