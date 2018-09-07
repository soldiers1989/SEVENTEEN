package com.seventeen.mapper;

import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SeOrderMapper extends CoreMapper<SeOrder> {

    ArrayList<OrderCenter> getSeOrders(@Param("userId") String admin,@Param("status") String status,@Param("remark") String remark,@Param("startTime") String startTime,@Param("endTime") String endTime);

    OrderCenter getOrderDetail(@Param("orderId")String orderId);

    ArrayList<OrderCenter> getNoReplyOrderList(@Param("id") String id,@Param("reply") String reply);

    OrderCenter noReplyOrder(@Param("id") String id,@Param("reply") String reply);


    @Select("select date_format(in_time, '%Y-%m-%d %H:%i:%s') inTime , date_format(out_time, '%Y-%m-%d %H:%i:%s') outTime from se_order so where so.status in('1','2','3')")
    List<SeOrder> getOrdersByStatus();


    @Update("update se_order so set so.status ='0' where so.id = #{id} ")
    void deleteByid(@Param("id") String id);

    @Select("select * from se_order so where so.out_time  = DATE_FORMAT(#{date},'%Y-%m-%d %H:%i:%s') and so.`status` in('2')")
    List<SeOrder> getCheckOut(@Param("date") String date);

    @Select("SELECT so.* FROM se_order_liver sol,se_order so WHERE sol.id_card = #{idCard} AND sol.order_id = so.id AND so.`status`='1'")
    List<SeOrder> getOrderByidCard(@Param("idCard")String idCard);
}