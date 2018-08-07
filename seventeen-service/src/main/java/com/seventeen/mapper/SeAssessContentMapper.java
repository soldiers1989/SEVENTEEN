package com.seventeen.mapper;

import com.seventeen.bean.SeAssessContent;
import com.seventeen.bean.WxTotalAssess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SeAssessContentMapper extends CoreMapper<SeAssessContent>{

    @Select("SELECT\n" +
            "content,\n" +
            "s.username createBy\n" +
            "FROM\n" +
            "se_assess_content sac,\n" +
            "sys_user s\n" +
            "WHERE\n" +
            "\tsac.assess_id =#{assessId} \n" +
            "\t and (parent_id is null or parent_id='') and sac.create_by = s.id\n" +
            "\n" +
            "UNION\n" +
            "SELECT\n" +
            "content,\n" +
            "s.username createBy\n" +
            "FROM\n" +
            "se_assess_content sac,\n" +
            "sys_user s\n" +
            "WHERE\n" +
            "\tsac.parent_id = #{assessId} and sac.create_by = s.id")
    List<SeAssessContent> getseAssessContent(@Param("assessId") String assessId);

    @Select("SELECT\n" +
            "\tcount(1)\n" +
            "FROM \n" +
            "\tse_assess_content sac,\n" +
            "\tsys_user su,\n" +
            "\tse_assess sa,\n" +
            "\tse_order so,\n" +
            "\tse_apartment sapm,\n" +
            "\tse_tag st\n" +
            "WHERE\n" +
            "\tsac.create_by = su.id\n" +
            "AND sac.parent_id IS NULL\n" +
            "and sac.assess_id = sa.content_id\n" +
            "And sa.order_id = so.id\n" +
            "And sa.status !='0'\n" +
            "and so.ap_id = sapm.id\n" +
            "and sapm.room_type = st.id")
    String getseContentTotalSize();

    @Select("SELECT\n" +
            "\tsu.username userName,\n" +
            "\tsu.avatar_url avatarUrl,\n" +
            "\tsu.phone userPhone,\n" +
            "\tsac.content,\n" +
            "\tROUND(AVG(sap.point)) totalPoint,\n" +
            "\tst.`name` roomName,\n" +
            "\tsa.create_time createTime\n" +
            "FROM \n" +
            "\tse_assess_content sac,\n" +
            "\tsys_user su,\n" +
            "\tse_assess sa,\n" +
            "\tse_assess_point sap,\n" +
            "\tse_order so,\n" +
            "\tse_apartment sapm,\n" +
            "\tse_tag st\n" +
            "WHERE\n" +
            "\tsac.create_by = su.id\n" +
            "AND sac.parent_id IS NULL\n" +
            "and sac.assess_id = sa.content_id\n" +
            "AND sa.assess_point_id = sap.id\n" +
            "And sa.order_id = so.id\n" +
            "and sa.status!='0'\n"+
            "and so.ap_id = sapm.id\n" +
            "and sapm.room_type = st.id\n" +
            "\n")
    List<WxTotalAssess.AssessContent> getseContentList();

}