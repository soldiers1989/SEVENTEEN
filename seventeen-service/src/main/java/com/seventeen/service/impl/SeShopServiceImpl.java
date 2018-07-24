package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.SeShop;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeShopMapper;
import com.seventeen.service.SeShopService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
@Slf4j
public class SeShopServiceImpl implements SeShopService {

    @Autowired
    private SeShopMapper seShopMapper;

    @Override
    public Result<List<SeShop>> getShopList(String remark, PageInfo pageInfo) {
        Result<List<SeShop>> result = new Result<>();
        try {
            SeShop seShop = new SeShop();
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            List<SeShop> seShops = seShopMapper.getShopList(remark);
            pageInfo.setTotal(page.getTotal());
            result.setData(seShops, pageInfo);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<SeShop> getShopDetail(String shopId) {
        Result<SeShop> result = new Result<>();
        try {
            SeShop seShop = new SeShop();
            seShop.setId(shopId);
            seShop = seShopMapper.selectByPrimaryKey(seShop);
            result.setData(seShop);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addShop(SeShop seShop) {
        Result<List<SeShop>> result = new Result<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            seShop.setId(IDGenerator.getId());
            seShop.setStatus("1");
            seShop.setCreateTime(DateUtil.now());
            seShop.setCreateBy(authentication.getName());
            seShopMapper.insert(seShop);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateShop(SeShop seShop) {
        Result result = new Result<>();
        try {
            seShopMapper.updateByPrimaryKey(seShop);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result deleteShop(String ids) {
        Result result = new Result<>();
        try {
            SeShop seShop = new SeShop();
            seShop.setId(ids);
            seShop.setStatus("0");
            seShopMapper.updateByPrimaryKeySelective(seShop);
        } catch (Exception e) {
            log.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    /**
     * @return
     */
    @Override
    public Result<List<SeShop>> getAllList() {
        SeShop seShop = new SeShop();
        seShop.setStatus("1");
        List<SeShop> select = seShopMapper.select(seShop);
        Result<List<SeShop>> result = new Result<>();
        result.setData(select);
        return result;
    }
}
