package com.seventeen.mapper;

import com.seventeen.bean.SeShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeShopMapper extends CoreMapper<SeShop>{

    @Select("<script> select ss.* from  se_shop  ss where 1=1 and status !='0'" +
            " <if test=\"remark != null and remark  != ''\">" +
            "     AND ( ss.`name` like CONCAT('%',#{remark},'%' )  or ss.address like CONCAT('%',#{remark},'%' ) or ss.phone like CONCAT('%',#{remark},'%' ) or ss.leader like\n" +
            "    CONCAT('%',#{remark},'%' ))" +
            " </if>" +
            "</script>")
    List<SeShop> getShopList(@Param("remark") String remark);

}