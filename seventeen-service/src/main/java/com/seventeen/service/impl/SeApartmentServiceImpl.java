package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.seventeen.bean.*;
import com.seventeen.bean.WxAppIndex.TypeRoom;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.*;
import com.seventeen.service.SeApartmentService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private SeAdviseMapper seAdviseMapper;

    @Autowired
    private SeApartmentGoodMapper seApartmentGoodMapper;

    @Autowired
    private SeApartmentImgMapper seApartmentImgMapper;

    @Autowired
    private SeApartmentPriceTypeMapper seApartmentPriceTypeMapper;

    @Override
    public Result<List<SeApartment>> getSeApartments(String status, String roomType, String remark, PageInfo pageInfo) {
        Result<List<SeApartment>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            ArrayList<SeApartment> seApartments = seApartmentMapper.getSeApartments(status, remark, roomType);
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
          /** 不物理删除图片
           * for (String id : split) {
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
            seApartmentImgMapper.deleteByApId(split);*/
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
            seTag.setStatus("1");
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
    public Result addTags(SeTag seTag) {
        Result result = new Result<>();

        try {
            SeTag seTag1 = new SeTag();
            seTag1.setStatus("1");
            seTag1.setName(seTag.getName());
            List<SeTag> seTags = seTagMapper.select(seTag1);
            if (seTags.size() > 0) {
                return new Result(500, "该类型数据重复");
            }
            seTag.setStatus("1");
            seTag.setId(IDGenerator.getId());
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
            /**
             * 删除房间类型关联
             */
            SeApartment seApartment = new SeApartment();
            seApartment.setRoomType(ids);
            List<SeApartment> seApartments = seApartmentMapper.select(seApartment);
            if (seApartments.size() > 0) {
                for (SeApartment apartment : seApartments) {
                    apartment.setRoomType(null);
                    seApartmentMapper.updateByPrimaryKey(apartment);
                }
            }
            seApartmentPriceTypeMapper.updateSeApartmentPriceTypeStatus("0", ids);

            SeTag seTag = new SeTag();
            seTag.setId(ids);
            seTag.setStatus("0");
            seTagMapper.updateByPrimaryKeySelective(seTag);

        } catch (Exception e) {
            logger.error("error",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addPriceType(ApartmentPriceRoom apartmentPriceRoom) {
        Result result = new Result<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            String roomTypeName = apartmentPriceRoom.getName();
            SeTag seTag = new SeTag();
            seTag.setName(roomTypeName);
            seTag.setStatus("1");
            seTag.setType("r");

            List<SeTag> seTags = seTagMapper.select(seTag);
            if (seTags.size() > 0) {
                return new Result(500, "该房间类型已存在");
            }

            seTag.setId(IDGenerator.getId());
            seTag.setCreateBy(authentication.getName());
            seTag.setCreateTime(DateUtil.now());
            seTagMapper.insert(seTag);

            List<ApartmentPriceRoom.Domain> domains = apartmentPriceRoom.getDomains();
            for (ApartmentPriceRoom.Domain domain : domains) {
                SeApartmentPriceType seApartmentPriceType = new SeApartmentPriceType();
                String price = domain.getPrice();
                List<String> time = domain.getTime();
                String priceTypeId = domain.getPriceTypeId();
                if (!StringUtils.isEmpty(price) && !StringUtils.isEmpty(priceTypeId) && time.size() != 0) {
                    seApartmentPriceType.setTagId(priceTypeId);
                    seApartmentPriceType.setApTypeId(seTag.getId());
                    seApartmentPriceType.setPrice(price);
                    seApartmentPriceType.setStartTime(time.get(0));
                    seApartmentPriceType.setEndTime(time.get(1));
                    seApartmentPriceType.setStatus("1");
                    seApartmentPriceType.setCreateTime(DateUtil.now());
                    seApartmentPriceType.setCreateBy(authentication.getName());
                    seApartmentPriceTypeMapper.insert(seApartmentPriceType);
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getPriceType() {

        Result<List<ResultApartmentPriceRoom>> result = new Result<>();
        try {


            ArrayList<ResultApartmentPriceRoom> resultApartmentPriceRooms = new ArrayList<>();
            SeTag seTag = new SeTag();
            seTag.setStatus("1");
            seTag.setType("r");
            List<SeTag> seTags = seTagMapper.select(seTag);
            for (SeTag set : seTags) {
                SeApartmentPriceType seApartmentPriceType = new SeApartmentPriceType();
                seApartmentPriceType.setApTypeId(set.getId());
                seApartmentPriceType.setStatus("1");
                List<SeApartmentPriceType> seApartmentPriceTypes = seApartmentPriceTypeMapper.select(seApartmentPriceType);

                for (SeApartmentPriceType apartmentPriceType : seApartmentPriceTypes) {
                    ResultApartmentPriceRoom resultApartmentPriceRoom = new ResultApartmentPriceRoom();
                    SeTag seTag1 = seTagMapper.selectByPrimaryKey(apartmentPriceType.getTagId());
                    resultApartmentPriceRoom.setName(set.getName());
                    resultApartmentPriceRoom.setRoomTypeId(set.getId());
                    resultApartmentPriceRoom.setPrice(apartmentPriceType.getPrice());
                    resultApartmentPriceRoom.setType(seTag1.getName());
                    resultApartmentPriceRoom.setTime(apartmentPriceType.getStartTime() + "至" + apartmentPriceType.getEndTime());
                    resultApartmentPriceRoom.setSize(seApartmentPriceTypes.size());
                    resultApartmentPriceRoom.setPriceTypeId(seTag1.getId());
                    resultApartmentPriceRooms.add(resultApartmentPriceRoom);
                }
                if (seApartmentPriceTypes.size() == 0) {
                    ResultApartmentPriceRoom resultApartmentPriceRoom = new ResultApartmentPriceRoom();
                    resultApartmentPriceRoom.setName(set.getName());
                    resultApartmentPriceRoom.setRoomTypeId(set.getId());
                    resultApartmentPriceRoom.setPrice("房间原价");
                    resultApartmentPriceRoom.setType("无房间价格类型");
                    resultApartmentPriceRoom.setSize(1);
                    resultApartmentPriceRooms.add(resultApartmentPriceRoom);
                }
            }
            result.setData(resultApartmentPriceRooms);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getPriceTypeDetail(String roomTypeId) {
        Result<ApartmentPriceRoom> result = new Result<>();
        try {
            ApartmentPriceRoom apartmentPriceRoom = new ApartmentPriceRoom();
            ArrayList<ApartmentPriceRoom.Domain> domains = new ArrayList();

            SeTag seTag = new SeTag();
            seTag.setId(roomTypeId);
            seTag.setStatus("1");
            seTag.setType("r");
            List<SeTag> seTags = seTagMapper.select(seTag);
            for (SeTag set : seTags) {
                apartmentPriceRoom.setName(set.getName());
                apartmentPriceRoom.setRoomTypeId(set.getId());
                SeApartmentPriceType seApartmentPriceType = new SeApartmentPriceType();
                seApartmentPriceType.setApTypeId(set.getId());
                seApartmentPriceType.setStatus("1");
                List<SeApartmentPriceType> seApartmentPriceTypes = seApartmentPriceTypeMapper.select(seApartmentPriceType);
                for (SeApartmentPriceType apartmentPriceType : seApartmentPriceTypes) {
                    ApartmentPriceRoom.Domain domain = new ApartmentPriceRoom.Domain();
                    SeTag seTag1 = seTagMapper.selectByPrimaryKey(apartmentPriceType.getTagId());
                    domain.setPriceTypeId(seTag1.getId());
                    domain.setType(seTag1.getName());
                    domain.setPrice(apartmentPriceType.getPrice());
                    domain.setTime(Arrays.asList(apartmentPriceType.getStartTime(), apartmentPriceType.getEndTime()));
                    domains.add(domain);
                }
                if (seApartmentPriceTypes.size() == 0) {
                    ApartmentPriceRoom.Domain domain = new ApartmentPriceRoom.Domain();
                    domains.add(domain);
                }
            }
            apartmentPriceRoom.setDomains(domains);
            result.setData(apartmentPriceRoom);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updatePriceType(ApartmentPriceRoom apartmentPriceRoom) {
        Result result = new Result<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            List<ApartmentPriceRoom.Domain> domains = apartmentPriceRoom.getDomains();
            for (ApartmentPriceRoom.Domain domain : domains) {
                String price = domain.getPrice();
                List<String> time = domain.getTime();
                String priceTypeId = domain.getPriceTypeId();
                if (!StringUtils.isEmpty(price) && !StringUtils.isEmpty(priceTypeId) && time.size() != 0) {
                    SeApartmentPriceType seApartmentPriceType = new SeApartmentPriceType();
                    seApartmentPriceType.setApTypeId(apartmentPriceRoom.getRoomTypeId());
                    seApartmentPriceType.setTagId(domain.getPriceTypeId());
                    List<SeApartmentPriceType> select = seApartmentPriceTypeMapper.select(seApartmentPriceType);
                    if (select.size() > 0) {
                        seApartmentPriceTypeMapper.updateSeApartmentPriceType(price,"1" ,time.get(0), time.get(1), apartmentPriceRoom.getRoomTypeId(), domain.getPriceTypeId());
                    } else {
                        seApartmentPriceType.setPrice(price);
                        seApartmentPriceType.setStartTime(time.get(0));
                        seApartmentPriceType.setEndTime(time.get(1));
                        seApartmentPriceType.setStatus("1");
                        seApartmentPriceType.setCreateTime(DateUtil.now());
                        seApartmentPriceType.setCreateBy(authentication.getName());
                        seApartmentPriceTypeMapper.insert(seApartmentPriceType);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<SeAdvise>> getAdviseList(String startTime, String endTime, PageInfo pageInfo) {
        Result result = new Result<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            List<SeAdvise> seAdviseList = seAdviseMapper.getSeAdviseList(startTime, endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(seAdviseList, pageInfo);
        } catch (Exception e) {
            logger.error("error",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addAdvise(SeAdvise seAdvise, SysUser sysUser) {
        Result result = new Result<>();
        try {
            seAdvise.setId(IDGenerator.getId());
            seAdvise.setCreateBy(sysUser.getUsername());
            seAdvise.setCreateTime(DateUtil.now());
            seAdvise.setUserId(sysUser.getId());
            seAdviseMapper.insert(seAdvise);
        } catch (Exception e) {
            logger.error("error",e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 获取店下所有类型的房间
     *
     * @param shop
     * @return
     */
    @Override
    public Result<List<TypeRoom>> getTypeRooms(String shop) {
        Result result = new Result<>();
        List<TypeRoom> typeRooms = seAdviseMapper.getTypeRooms(shop);
        result.setData(typeRooms);
        return result;
    }
}
