package com.seventeen.mapper;

import com.seventeen.bean.SeOrder;
import com.seventeen.bean.SeOrderPay;
import com.seventeen.bean.SeShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeOrderPayMapper extends CoreMapper<SeOrderPay>{

    @Update("UPDATE `se_order_pay` a SET a.`status`=3,a.`out_pay_time`=NOW() WHERE a.id= #{orderId}")
    void updateCancel(@Param("orderId") String orderPayId);




    @Select("select so.* from se_order so,se_order_pay sop where sop.se_order_id = so.id  and sop.id= #{payId}")
    List<SeOrder> getOrder(@Param("payId") String payId);




}