package com.seventeen.mapper;


import com.seventeen.bean.core.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper extends CoreMapper<SysUser> {
    @Select("SELECT * FROM sys_user pct WHERE pct.id =#{id}")
    SysUser selectElementsByIds(@Param("id") String id);
}
