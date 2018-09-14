package com.seventeen.mapper;


import com.seventeen.bean.SeApartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.access.method.P;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeApartmentMapper extends CoreMapper<SeApartment>{

    ArrayList<SeApartment> getSeApartments(@Param("status") String status,@Param("remark")String remark,@Param("roomType") String roomType);

    void deleteApartment(@Param("ids") String[] ids);

    @Select("select * from se_apartment where room_type = #{roomType} and status !='0'")
    ArrayList<SeApartment> getCanUseApartments(String roomType);

    ArrayList<String> getApartmentByTime(@Param("inTime") String startTime, @Param("outTime") String endTime,@Param("roomType")  String roomType);

    @Select("select * from se_apartment where ap_num = #{apNum} and status in ('1','2')")
    List<SeApartment> apartmentCheck(@Param("apNum") String apNum);

}