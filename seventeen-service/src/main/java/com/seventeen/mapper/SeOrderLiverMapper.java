package com.seventeen.mapper;


import com.seventeen.bean.SeOrderLiver;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeOrderLiverMapper extends CoreMapper<SeOrderLiver>{

    @Select("select * from se_order_liver sol where sol.order_id =#{orderid} and liver like CONCAT('%',#{liver},'%' )'")
    SeOrderLiver selectByOrderIs(@Param("orderid") String orderid,@Param("liver") String liver);
}