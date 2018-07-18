package com.seventeen.mapper;


import com.seventeen.bean.SeApartmentPriceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeApartmentPriceTypeMapper extends CoreMapper<SeApartmentPriceType>{


    @Update("UPDATE se_apartment_price_type SET price =  #{price},start_time =  #{startTime},end_time =  #{endTime} WHERE ap_type_id = #{roomTypeId} AND tag_id = #{priceTypeId} AND status =  #{status}")
    public void updateSeApartmentPriceType(@Param("price")String price,@Param("status")String status,@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("roomTypeId") String roomTypeId, @Param("priceTypeId")String priceTypeId );

    @Update("UPDATE se_apartment_price_type SET status =#{status} WHERE tag_id = #{id} or  ap_type_id = #{id}")
    public void updateSeApartmentPriceTypeStatus(@Param("status")String status, @Param("id")String id );


}