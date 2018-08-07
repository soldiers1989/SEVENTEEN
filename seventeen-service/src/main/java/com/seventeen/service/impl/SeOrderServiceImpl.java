package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.*;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.*;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.MyConfig;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Author: csk
 * @Date: 2018/5/8 14:42
 */
@Service
public class SeOrderServiceImpl implements SeOrderService {
    private final Logger logger = LoggerFactory.getLogger(SeOrderServiceImpl.class);

    @Autowired
    private SeOrderMapper seOrderMapper;
    @Autowired
    private SeOrderLiverMapper seOrderLiverMapper;
    @Autowired
    private SeUserAttestationMapper seUserAttestationMapper;
    @Autowired
    private SeOrderCalendarMapper seOrderCalendarMapper;
    @Autowired
    private SeTagMapper seTagMapper;
    @Autowired
    private SeApartmentMapper seApartmentMapper;

    /**
     * @param status
     * @param remark
     * @param pageInfo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Result<List<OrderCenter>> getOrderList(SysUser sysUser, String status, String remark, PageInfo pageInfo, String startTime, String endTime) {
        Result<List<OrderCenter>> result = new Result<>();

        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);

            String admin = "";
            /**
             * 小程序用户
             */
            if (sysUser.getRoleIds().contains("admin")) {
                admin = sysUser.getId();
            }

            ArrayList<OrderCenter> orderCenters = seOrderMapper.getSeOrders(admin, status, remark, startTime, endTime);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> getOrderDetail(String orderId) {

        Result<OrderCenter> result = new Result<>();
        try {
            OrderCenter orderCenter = seOrderMapper.getOrderDetail(orderId);
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<List<OrderCenter>> noReplyOrderList(String reply, SysUser sysUser, PageInfo pageInfo) {

        Result<List<OrderCenter>> result = new Result<>();
        try {
            Page page = PageHelper.startPage(pageInfo.getPageNum(),
                    pageInfo.getPageSize(), true);
            String id = sysUser.getId();
            ArrayList<OrderCenter> orderCenters = seOrderMapper.getNoReplyOrderList(id, reply);
            pageInfo.setTotal(page.getTotal());
            result.setData(orderCenters, pageInfo);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<OrderCenter> noReplyOrder(String orderId, SysUser sysUser) {
        Result<OrderCenter> result = new Result<>();
        try {

            OrderCenter orderCenter = seOrderMapper.noReplyOrder(orderId, "0");
            result.setData(orderCenter);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> deleteOrderByWx(String id, SysUser sysUser) {
        Result<String> result = new Result<>();
        try {
            SeOrder seOrder = new SeOrder();
            seOrder.setId(id);
            SeOrder seOrder1 = seOrderMapper.selectByPrimaryKey(seOrder);
            if ("4".equals(seOrder1.getStatus()) || "5".equals(seOrder1.getStatus())) {
                seOrderMapper.deleteByid(id);
            } else {
                return result.setResultCode(300).setMessage("只能删除退订和订单完成的订单");
            }
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public ResponseEntity setOrder(SysUser sysUser, OrderInfo orderInfo) {
        String orderId = IDGenerator.getId();//RandomStringUtils.randomAlphanumeric(23);
        System.out.println("订单号:" + orderId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(LocalDateTime.now().format(dateTimeFormatter));
        Result result = new Result();
        //List<SeOrder> seList=new ArrayList<>();
        if (!orderInfo.getRoomNum().equals("1")) {//多间时候需要拆分订单
            List<SeOrder> seList = new ArrayList<>();


        } else {
            SeOrder se = new SeOrder();
            se.setId(orderId);
            se.setApId(orderInfo.getRoomId());
            se.setArriveTime(orderInfo.getPlanTime());
            se.setOrderTime(LocalDateTime.now().format(dateTimeFormatter));
            se.setCreateTime(LocalDateTime.now().format(dateTimeFormatter));
            se.setCouponId(orderInfo.getCouponId());
            se.setInTime(orderInfo.getStartTime());
            se.setOutTime(orderInfo.getEndTime());
            se.setUserId(sysUser.getId());
            se.setPriceTagId(orderInfo.getTagId());
            se.setPrice(orderInfo.getPrice());
            se.setCreateBy(orderInfo.gettName());
            se.setCreatorPhone(orderInfo.getPhone());
            se.setStatus("-1");
            se.setIsReply("0");
            se.setLockPwd(String.valueOf((int) ((Math.random() * 9 + 1) * 10000000)));

            seOrderMapper.insert(se);

            SeOrderLiver ol = new SeOrderLiver();
            ol.setId(IDGenerator.getId());
            ol.setOrderId(orderId);
            ol.setCreateBy(se.getCreateBy());
            ol.setCreateTime(se.getCreateTime());
            ol.setPhone(se.getCreatorPhone());
            ol.setLiver(se.getCreateBy());
            ol.setUpdateTime(LocalDateTime.now().format(dateTimeFormatter));
            SeUserAttestation su = new SeUserAttestation();
            su.setUserId(sysUser.getId());
            SeUserAttestation seUserAttestation = seUserAttestationMapper.selectOne(su);
            ol.setIdCard(seUserAttestation.getIdCode());
            seOrderLiverMapper.insert(ol);
        }


        //本系统业务下单生产订单ID

        //本次订单随机串
        String nonceStr = RandomStringUtils.randomAlphanumeric(32);
        try {
            MyConfig config = new MyConfig();

            WXPay wxpay = new WXPay(config);

            Map<String, String> data = new HashMap<>();
            data.put("fee_type", "CNY");
            data.put("body", "17inn订房");//商品描述
            data.put("nonce_str", nonceStr);//随机字符串
            data.put("notify_url", "https://www.17inn.com/seventeen/WX/callBack");//异步回调api
            data.put("spbill_create_ip", "39.107.111.100");//支付ip
            data.put("out_trade_no", orderId);//商品订单号
            data.put("total_fee", "1");//真实金额orderInfo.getFee()
            data.put("trade_type", "JSAPI");//JSAPI、h5调用
            data.put("openid", sysUser.getOpenid());//支付用户openid


            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println("---预订单返回--\n" + resp);
            //处理加密返回给前端
            String timeStamp = String.valueOf(System.currentTimeMillis());
            String paySign = "appId=" + resp.get("appid") + "&nonceStr=" + resp.get("nonce_str") + "&package=prepay_id=" + resp.get("prepay_id") + "&signType=MD5&timeStamp=" + timeStamp + "&key=1TZt37EvkxJW8ctbVT9IPZQnYpISsgLy";
            System.out.println(paySign);
            paySign = MD5.MD5Encode(paySign, "UTF-8");

            Map<String, String> resMap = new HashMap<>();
            resMap.put("appId", resp.get("appid"));
            resMap.put("nonceStr", resp.get("nonce_str"));
            resMap.put("package", "prepay_id=" + resp.get("prepay_id"));
            resMap.put("paySign", paySign);
            resMap.put("timeStamp", timeStamp);


            result.setData(resMap);
            System.out.println(resMap);

        } catch (Exception e) {
            logger.error("下的错误", e);
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 支付后修改订单状态
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(String orderId) {

        SeOrder se = new SeOrder();
        se.setId(orderId);
        se = seOrderMapper.selectOne(se);
        se.setStatus("1");
        seOrderMapper.updateByPrimaryKey(se);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderCalendar() {
        Date firstday, lastday;
        try {
            SeTag seTag = new SeTag();
            seTag.setStatus("1");
            seTag.setType("r");
            List<SeTag> roomTypes = seTagMapper.select(seTag);
            for (SeTag roomType : roomTypes) {
                String id = roomType.getId();

                SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                seOrderCalendar.setRoomTypeId(id);
                int i = seOrderCalendarMapper.selectCount(seOrderCalendar);
                if (i > 0) {
                    Calendar instance = Calendar.getInstance();
                    instance.add(Calendar.MONTH, 3);
                    instance.set(Calendar.DAY_OF_MONTH, 1);
                    firstday = instance.getTime();
                    instance = Calendar.getInstance();
                    instance.add(Calendar.MONTH, 4);
                    instance.set(Calendar.DAY_OF_MONTH, 0);
                    lastday = instance.getTime();

                    int month = instance.get(Calendar.MONTH) + 1;
                    int year = instance.get(Calendar.YEAR);
                    seOrderCalendar.setMonth(month < 10 ? "0" + String.valueOf(month) : String.valueOf(month));
                    seOrderCalendar.setYear(String.valueOf(year));
                    i = seOrderCalendarMapper.selectCount(seOrderCalendar);
                    if (i <= 0) {
                        addThreeMonthCalendar(id, firstday, lastday);
                    }

                } else {
                    Calendar instance = Calendar.getInstance();
                    instance.set(Calendar.DAY_OF_MONTH, 1);
                    firstday = instance.getTime();
                    instance = Calendar.getInstance();
                    instance.add(Calendar.MONTH, 3);
                    instance.set(Calendar.DAY_OF_MONTH, 0);
                    lastday = instance.getTime();

                    addThreeMonthCalendar(id, firstday, lastday);
                }
            }
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public Result getOrderDate(String roomType, SysUser sysUser) {
        Result result = new Result();

        try {
            ArrayList seApartments = seApartmentMapper.getCanUseApartments(roomType);
            seApartments.size();
            SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
            seOrderCalendar.setRoomTypeId(roomType);
            seOrderCalendar.setOrders(seApartments.size());
            List<String> seOrderCalendars = seOrderCalendarMapper.getOrderDate(seOrderCalendar);
            result.setData(seOrderCalendars);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result cancelOrder(String order) {
        Result result = new Result();
        try {
            SeOrder seOrder = new SeOrder();
            seOrder.setId(order);
            SeOrder seOrder1 = seOrderMapper.selectByPrimaryKey(seOrder);
            seOrder1.setStatus("3");
            seOrderMapper.updateByPrimaryKeySelective(seOrder1);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result cancelOrderConfirm(String order) {
        Result result = new Result();
        try {
            List<String> list = new ArrayList();
            List<SeOrderCalendar> addList = new ArrayList();

            SeOrder seOrder = new SeOrder();
            seOrder.setId(order);
            SeOrder seOrder1 = seOrderMapper.selectByPrimaryKey(seOrder);
            String apId = seOrder1.getApId();

            SeApartment seApartment = new SeApartment();
            seApartment.setId(apId);
            seApartment = seApartmentMapper.selectByPrimaryKey(seApartment);

            String inTime = seOrder1.getInTime().replace(".0","");
            String outTime = seOrder1.getOutTime().replace(".0","");
            LocalDate startDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(inTime, DateUtil.DEFAULT_DATETIME_PATTERN));
            LocalDate endDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(outTime, DateUtil.DEFAULT_DATETIME_PATTERN));

            long distance = ChronoUnit.DAYS.between(startDate,endDate);
            if (distance >= 1) {
                Stream.iterate(startDate, d -> {
                    return d.plusDays(1);
                }).limit(distance + 1).forEach(f -> {
                    list.add(f.toString());
                });
            }
            for (int i = 0; i <list.size()-1 ; i++) {
                String date = list.get(i);
                SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                String[] dateArr = date.split("-");
                seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                seOrderCalendar.setYear(dateArr[0]);
                seOrderCalendar.setMonth(dateArr[1]);
                seOrderCalendar.setDay(dateArr[2]);

                seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                seOrderCalendar.setOrders(seOrderCalendar.getOrders()-1);
                seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);
            }
            seOrderMapper.updateByPrimaryKeySelective(seOrder1);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }


    @Transactional(rollbackFor = Exception.class)
    public void addThreeMonthCalendar(String id, Date inDate, Date outDate) {
        List<String> list = new ArrayList();
        List<SeOrderCalendar> addList = new ArrayList();


        LocalDate startDate = DateUtil.toLocalDate(inDate);
        LocalDate endDate = DateUtil.toLocalDate(outDate);
        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance >= 1) {
            Stream.iterate(startDate, d -> {
                return d.plusDays(1);
            }).limit(distance + 1).forEach(f -> {
                list.add(f.toString());
            });
        }
        for (String date : list) {
            SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
            String[] dateArr = date.split("-");
            seOrderCalendar.setId(IDGenerator.getId());
            seOrderCalendar.setRoomTypeId(id);
            seOrderCalendar.setYear(dateArr[0]);
            seOrderCalendar.setMonth(dateArr[1]);
            seOrderCalendar.setDay(dateArr[2]);
            seOrderCalendar.setOrders(0);
            addList.add(seOrderCalendar);
        }
        if (!list.isEmpty()) {
            seOrderCalendarMapper.addSeOrderCalendarList(addList);
        }
        return;
    }
}
