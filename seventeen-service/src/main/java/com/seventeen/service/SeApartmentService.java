package com.seventeen.service;

import com.seventeen.bean.SeApartment;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */

public interface SeApartmentService{

    Result<List<SeApartment>> getSeApartments(String status,String remark, PageInfo pageInfo);

    Result<String> deleteApartment(String ids);

}
