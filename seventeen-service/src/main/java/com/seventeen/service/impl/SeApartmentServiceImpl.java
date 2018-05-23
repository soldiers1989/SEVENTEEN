package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.SeApartment;
import com.seventeen.bean.SeTag;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeApartmentMapper;
import com.seventeen.mapper.SeTagMapper;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.FileUploadUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    private SeTagMapper seTagMapper;


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
    @Transactional
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

    @Override
    public Result<List<SeTag>> getTags(String type, String name) {
        Result<List<SeTag>> result = new Result<>();

        try {
            SeTag seTag = new SeTag();
            seTag.setType(type);
            seTag.setName(name);
            List<SeTag> seTags = seTagMapper.select(seTag);
            result.setData(seTags);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Result<SeApartment> addApartment(SeApartment seApartment) {
        Result<SeApartment> result = new Result<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            seApartment.setId(IDGenerator.getId());
            seApartment.setCreateBy(authentication.getName());
            seApartment.setStatus("1");
            seApartment.setCreateTime(DateUtil.now());
            seApartmentMapper.insert(seApartment);
            result.setData(seApartment);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<Boolean> apartmentCheck(String apNum) {
        Result<Boolean> result = new Result<>();
        Boolean flag = false;
        try {
            SeApartment seApartment = new SeApartment();
            seApartment.setApNum(apNum);
            seApartment.setStatus("1");
            List<SeApartment> select = seApartmentMapper.select(seApartment);
            if (select.size() > 0) {
                result.setData(flag = true);
            }
            result.setData(flag);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }
}
