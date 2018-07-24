package com.seventeen.service;

import com.seventeen.bean.SeCoupon;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;

import java.util.List;

public interface UserVerify {
    /**
     * @return
     */
    Result verify(SysUser sysUser);

    /**
     * 是有已经通过了实名认证
     * @param sysUser
     * @return
     */
    Result isVerify(SysUser sysUser);
}
