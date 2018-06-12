package com.seventeen.mapper;

import com.seventeen.bean.core.SysUser;
import com.seventeen.bean.core.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface SysUserRoleMapper extends CoreMapper<SysUserRole> {

    @Select("<script> SELECT " +
            "su.username username, " +
            "su.phone phone, " +
            "su.last_order_time lastOrderTime,  " +
            "su.last_login_time lastLoginTime, " +
            "su.create_date createDate " +
            "FROM " +
            "sys_user su, " +
            "sys_user_role sur " +
            "WHERE " +
            "sur.role_id = '20170802115412744304269' " +
            "AND sur.user_id = su.id " +
            "<if test=\"status != null and status  != ''\">" +
            "  AND (su.username like CONCAT('%',#{status},'%' ) or su.phone like CONCAT('%',#{status},'%' ) or su.last_order_time like " +
            "  CONCAT('%',#{status},'%' ) or su.last_login_time like CONCAT('%',#{status},'%' ) or su.create_date like CONCAT('%',#{status},'%' ) ) " +
            "</if>" +
            "</script> ")
    ArrayList<SysUser> findList(@Param("status") String status);
}
