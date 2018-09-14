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

    @Select("select year||'-'||CONVERT(month,SIGNED)||'-'||CONVERT(day,SIGNED) from se_order_calendar where room_type_id =#{seOrderCalendar.roomTypeId} and orders >= #{seOrderCalendar.orders}")
    List<String> getOrderDateFormat(@Param("seOrderCalendar") SeOrderCalendar seOrderCalendar);

    @Select("select year||'-'|| month||'-'||day from se_order_calendar where room_type_id =#{seOrderCalendar.roomTypeId} and orders >= #{seOrderCalendar.orders}")
    List<String> getOrderDate(@Param("seOrderCalendar") SeOrderCalendar seOrderCalendar);

}