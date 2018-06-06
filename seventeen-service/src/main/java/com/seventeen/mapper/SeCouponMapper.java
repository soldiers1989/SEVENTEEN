package com.seventeen.mapper;

import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeCouponMapper extends CoreMapper<SeCoupon> {

    ArrayList<SeCoupon> getSeCoupons(@Param("status") String status, @Param("remark") String remark);

    @Delete("<script> delete  from se_coupon WHERE id IN\n" +
            "        <foreach collection='ids' index='index' item='id' open='(' separator=',' close=')'>\n" +
            "            #{id}\n" +
            "        </foreach> " +
            "</script>")
    void deleteCoupon(@Param("ids") String[] split);

    @Select("select * from  se_coupon where end_time < #{date} and status ='1'")
    List<SeCoupon> selectCouponEveryDay(@Param("date") String date);

    ArrayList<CouponLog> getCouponLog(String status, String remark, String startTime, String endTime);

}