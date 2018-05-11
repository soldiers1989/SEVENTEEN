package com.seventeen.mapper;


import com.seventeen.bean.SeApartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@SuppressWarnings({"ALL", "AlibabaClassMustHaveAuthor"})
@Mapper
public interface SeApartmentMapper{

    ArrayList<SeApartment> getSeApartments(@Param("status") String status,@Param("remark")String remark);

    void deleteApartment(@Param("ids") String[] ids);

}