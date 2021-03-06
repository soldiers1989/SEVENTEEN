package com.seventeen.mapper;

import com.seventeen.bean.CouponLog;
import com.seventeen.bean.SeCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeCouponMapper extends CoreMapper<SeCoupon> {

    /**
     * @param status
     * @param remark
     * @return
     */
    ArrayList<SeCoupon> getSeCoupons(@Param("status") String status, @Param("remark") String remark);

    @Update("<script> update se_coupon set status ='0' WHERE id IN\n" +
            "        <foreach collection='ids' index='index' item='id' open='(' separator=',' close=')'>\n" +
            "            #{id}\n" +
            "        </foreach> " +
            "</script>")
    void deleteCoupon(@Param("ids") String[] split);

    @Select("select * from  se_coupon where end_time < #{date} and status ='1'")
    List<SeCoupon> selectCouponEveryDay(@Param("date") String date);

    ArrayList<CouponLog> getCouponLog(@Param("status") String status, @Param("remark") String remark, @Param("startTime") String startTime, @Param("endTime") String endTime);

    ArrayList<SeCoupon> couponListWx(@Param("status") String status, @Param("userId") String userID);

    @Select("select sc.id from se_coupon sc where sc.status ='1' and sc.send_type like '%新用户%'")
    List<String> getNewClientCoupon();

    @Select("select count(1) from se_coupon sc ,se_coupon_price_type scp where sc.id = scp.coupon_id and scp.tag_id =#{roomType} and sc.`status`='1'")
    int getCouponByRoomType(@Param("roomType") String roomType);

    ArrayList<SeCoupon> getCouponByOrderCanUse(@Param("userId") String id, @Param("roomType") String roomType, @Param("containWeekEnd") int containWeekEnd);

    @Update("UPDATE `se_user_coupon` SET `status`=#{status},update_time=#{updateTime} WHERE user_id=#{userId} AND coupon_id={couponId}")
    void updateCouponStatus(@Param("status") String status,@Param("updateTime") String updateTime,@Param("userId") String userId,@Param("couponId") String couponId);
}