package com.seventeen.mapper;


import com.seventeen.bean.SeApartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface SeApartmentMapper extends CoreMapper<SeApartment>{

    ArrayList<SeApartment> getSeApartments(@Param("status") String status,@Param("remark")String remark);

    void deleteApartment(@Param("ids") String[] ids);

}