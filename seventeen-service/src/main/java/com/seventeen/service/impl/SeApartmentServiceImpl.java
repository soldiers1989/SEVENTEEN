package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.*;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeApartmentGoodMapper;
import com.seventeen.mapper.SeApartmentImgMapper;
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
import java.util.stream.Collectors;

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

    @Autowired
    private SeApartmentGoodMapper seApartmentGoodMapper;

    @Autowired
    private SeApartmentImgMapper seApartmentImgMapper;


    @Override
    public Result<List<SeApartment>> getSeApartments(String status,String roomType,String remark, PageInfo pageInfo) {
        Result<List<SeApartment>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<SeApartment> seApartments = seApartmentMapper.getSeApartments(status, remark,roomType);
            pageInfo.setTotal(page.getTotal());
            result.setData(seApartments, pageInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteApartment(String ids) {
        Result<String> result = new Result<>();
        try {
            String[] split = ids.split(",");
             seApartmentMapper.deleteApartment(split);
            for (String id : split) {
                List<SeApartmentImg> seApartmentImgs = seApartmentImgMapper.selectByApids(id);
                for (SeApartmentImg seApartmentImg : seApartmentImgs) {
                    Path path = Paths.get(FileUploadUtil.roomImg, seApartmentImg.getApId());
                    String fileSuffix = seApartmentImg.getName().substring(seApartmentImg.getName().lastIndexOf("."), seApartmentImg.getName().length());
                    String fileName = seApartmentImg.getId();
                    Path path_mix = Paths.get(FileUploadUtil.roomImg, fileName + "_mix" + fileSuffix);
                    if (Files.exists(path)) {
                        Files.delete(path);
                    }
                    if (Files.exists(path_mix)) {
                        Files.delete(path_mix);
                    }
                }
            }
            seApartmentImgMapper.deleteByApId(split);
        } catch (Exception e) {
            logger.error("error", e);
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
    @Transactional(rollbackFor = Exception.class)
    public Result<SeApartment> addApartment(SeApartment seApartment) {
        Result<SeApartment> result = new Result<>();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            seApartment.setId(IDGenerator.getId());
            seApartment.setCreateBy(authentication.getName());
            seApartment.setStatus("1");
            seApartment.setCreateTime(DateUtil.now());
            seApartmentMapper.insert(seApartment);

            ArrayList<String> goods = seApartment.getGood();
            for (String good : goods) {
                SeApartmentGood seApartmentGood = new SeApartmentGood();
                seApartmentGood.setTagId(good);
                seApartmentGood.setApId(seApartment.getId());
                seApartmentGood.setId(IDGenerator.getId());
                seApartmentGood.setCreateTime(DateUtil.now());
                seApartmentGood.setCreateBy(authentication.getName());
                seApartmentGoodMapper.insert(seApartmentGood);
            }
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

    @Override
    public Result<SeApartmentDetail> getApartmentDetail(String apNum) {

        Result<SeApartmentDetail> result = new Result<>();

        try {
            SeApartmentDetail seApartmentDetail = new SeApartmentDetail();

            SeApartment seApartment = seApartmentMapper.selectByPrimaryKey(apNum);
            List<SeApartmentImg> seApartmentImgs = seApartmentImgMapper.selectByApids(apNum);
            List<SeApartmentGood> seApartmentGoods = seApartmentGoodMapper.selectByApids(apNum);

            seApartmentDetail.setSeApartment(seApartment);
            seApartmentDetail.setSeApartmentImg(seApartmentImgs);
            seApartmentDetail.setGood(seApartmentGoods.stream().map(SeApartmentGood::getTagId).collect(Collectors.toList()));
            result.setData(seApartmentDetail);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<SeApartment> updateApartment(SeApartment seApartment) {
        Result<SeApartment> result = new Result<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            seApartment.setCreateBy(authentication.getName());
            seApartment.setCreateTime(DateUtil.now());
            seApartmentMapper.updateByPrimaryKey(seApartment);

            ArrayList<String> goods = seApartment.getGood();

            seApartmentGoodMapper.deleteByApid(seApartment.getId());
            for (String good : goods) {
                SeApartmentGood seApartmentGood = new SeApartmentGood();
                seApartmentGood.setTagId(good);
                seApartmentGood.setApId(seApartment.getId());
                seApartmentGood.setId(IDGenerator.getId());
                seApartmentGood.setCreateTime(DateUtil.now());
                seApartmentGood.setCreateBy(authentication.getName());
                seApartmentGoodMapper.insert(seApartmentGood);
            }
            result.setData(seApartment);
        } catch (Exception e) {
            logger.error("error", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addTags(String type, String name) {
        Result result = new Result<>();

        try {
            SeTag seTag = new SeTag();
            seTag.setId(IDGenerator.getId());
            seTag.setType(type);
            seTag.setName(name);
            seTagMapper.insert(seTag);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> deleteTag(String ids) {
        Result result = new Result<>();
        try {
            SeTag seTag = new SeTag();
            seTag.setId(ids);
            seTagMapper.delete(seTag);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }
}
