package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.SeApartment;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeApartmentMapper;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
public class SeApartmentServiceImpl implements SeApartmentService {
    private final Logger logger = LoggerFactory.getLogger(SeApartmentServiceImpl.class);

    @Autowired
    private SeApartmentMapper seApartmentMapper;


    @Override
    public Result<List<SeApartment>> getSeApartments(String status, String remark, PageInfo pageInfo) {
        Result<List<SeApartment>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<SeApartment> seApartments = seApartmentMapper.getSeApartments(status, remark);
            pageInfo.setTotal(page.getTotal());
            result.setData(seApartments, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> deleteApartment(String ids) {
        Result<String> result = new Result<>();
        try {
            String[] split = ids.split(",");
            seApartmentMapper.deleteApartment(split);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }
}
