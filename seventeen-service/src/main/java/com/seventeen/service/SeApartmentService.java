package com.seventeen.service;

import com.seventeen.bean.*;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */

public interface SeApartmentService{

    Result<List<SeApartment>> getSeApartments(String status,String roomType,String remark, PageInfo pageInfo);

    Result<String> deleteApartment(String ids);

    /***
     *
     * @param type
     * @param name
     * @return
     */
    Result<List<SeTag>> getTags(String type, String name);

    /***
     *
     * @param seApartment
     * @return
     */
    Result<SeApartment> addApartment(SeApartment seApartment);

    /**
     *
     * @param apNum
     * @return
     */
    Result<Boolean> apartmentCheck(String apNum);

    /**
     *
     * @param apNum
     * @return
     */
    Result<SeApartmentDetail> getApartmentDetail(String apNum);

    /**
     *
     * @param seApartment
     * @return
     */
    Result<SeApartment> updateApartment(SeApartment seApartment);

    Result addTags(SeTag seTag);

    Result<String> deleteTag(String ids);

    Result addPriceType(ApartmentPriceRoom seApartmentPriceType);

    Result getPriceType();

    Result getPriceTypeDetail(String roomTypeId);

    Result updatePriceType(ApartmentPriceRoom ruleRoomForm);

    Result<List<SeAdvise>> getAdviseList(String startTime, String endTime, PageInfo pageInfo);

    /**
     *
     * @param seAdvise
     * @param sysUser
     * @return
     */
    Result addAdvise(SeAdvise seAdvise, SysUser sysUser);

}
