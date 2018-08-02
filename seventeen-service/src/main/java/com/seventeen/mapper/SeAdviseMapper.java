package com.seventeen.mapper;

import com.seventeen.bean.RoomTypePirce;
import com.seventeen.bean.SeAdvise;
import com.seventeen.bean.WxAppIndex.TypeRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAdviseMapper extends CoreMapper<SeAdvise> {

    @Select("<script> select sc.* from  se_advise  sc where 1=1 " +
            " <if test=\"startTime != null and startTime  != ''\">" +
            "     AND sc.create_time &gt;= #{startTime}" +
            " </if>" +
            " <if test=\"endTime != null and endTime  != ''\">" +
            "     AND sc.create_time   &lt;= #{endTime} " +
            " </if>" +
            "</script>")
    List<SeAdvise> getSeAdviseList(@Param("startTime")String startTime, @Param("endTime")String endTime);



    @Select("SELECT  a.room_type,st.`name`,a.price,a.area,a.remark,i.url AS imgUrl,MAX(a.create_time) " +
            "FROM se_shop ss,`se_apartment` a   " +
            "LEFT JOIN se_tag st ON a.room_type = st.id " +
            "LEFT JOIN `se_apartment_img` i ON i.ap_id=a.id " +
            "WHERE a.shop_id = ss.`id` AND ss.id = #{shopId} AND a.status!=0" +
            "GROUP BY a.room_type ")
    List<TypeRoom> getTypeRooms(@Param("shopId")String shopId);

    @Select("SELECT a.`price`,b.name,a.tag_id FROM `se_apartment_price_type` a " +
            "INNER JOIN `se_tag` b ON a.`tag_id`=b.id " +
            "WHERE a.ap_type_id=#{typeCode} " +
            "ORDER BY b.id ASC")
    List<RoomTypePirce> getTypePiece(@Param("typeCode")String typeCode);
}