package com.seventeen.service;


import com.seventeen.bean.SeShop;
import com.seventeen.core.Result;
import com.seventeen.util.PageInfo;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/4/24 18:10
 */
public interface SeShopService {

    /**
     *
     * @param remark
     * @param pageInfo
     * @return
     */
    Result<List<SeShop>> getShopList(String remark, PageInfo pageInfo);

    /**
     *
     * @param shopId
     * @return
     */
    Result<SeShop> getShopDetail(String shopId);

    /**
     *
     * @param seShop
     * @return
     */
    Result addShop(SeShop seShop);


    /**
     *
     * @param seShop
     * @return
     */
    Result updateShop(SeShop seShop);

    /**
     *
     * @param ids
     * @return
     */
    Result deleteShop(String ids);

    /**

     * @return
     */
    Result<List<SeShop>> getAllList();

}
