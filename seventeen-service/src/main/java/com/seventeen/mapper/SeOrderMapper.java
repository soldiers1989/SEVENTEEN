package com.seventeen.mapper;

import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.SeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface SeOrderMapper extends CoreMapper<SeOrder> {

    ArrayList<OrderCenter> getSeOrders(@Param("userId") String admin,@Param("status") String status,@Param("remark") String remark,@Param("startTime") String startTime,@Param("endTime") String endTime);

    OrderCenter getOrderDetail(@Param("orderId")String orderId);

    ArrayList<OrderCenter> getNoReplyOrderList(@Param("id") String id,@Param("reply") String reply);

    OrderCenter noReplyOrder(@Param("id") String id,@Param("reply") String reply);





    @Update("update se_order so set so.status ='0' where so.id = #{id} ")
    void deleteByid(@Param("id") String id);
}