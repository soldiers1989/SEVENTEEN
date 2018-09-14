package com.seventeen.mapper;

import com.seventeen.bean.SeOrderCalendar;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeOrderCalendarMapper extends CoreMapper<SeOrderCalendar> {

    @Insert("<script>"
            +"INSERT INTO se_order_calendar  ( id,room_type_id,year,month,day,orders ) VALUES"
            + "<foreach item='item' index='index' collection='list' separator=',' >" + "(#{item.id},#{item.roomTypeId},#{item.year},#{item.month},#{item.day},#{item.orders})"
            + "</foreach>"
            +"</script>")
    void addSeOrderCalendarList(@Param("list") List seOrderCalendars);

    @Select("select s.time from (select year||'-'||CONVERT(month,SIGNED)||'-'||CONVERT(day,SIGNED) as time  from se_order_calendar where room_type_id =#{seOrderCalendar.roomTypeId} and orders >= #{seOrderCalendar.orders}) s where s.time >=#{time}")
    List<String> getOrderDateFormat(@Param("seOrderCalendar") SeOrderCalendar seOrderCalendar,@Param("time") String time);

    @Select("select s.time from (select year||'-'|| month||'-'||day as time  from se_order_calendar where room_type_id =#{seOrderCalendar.roomTypeId} and orders >= #{seOrderCalendar.orders}) s where s.time >= #{inTime} and s.time < #{outTime}")
    List<String> getOrderDate(@Param("seOrderCalendar") SeOrderCalendar seOrderCalendar,@Param("inTime") String inTime,@Param("outTime") String outTime);

}