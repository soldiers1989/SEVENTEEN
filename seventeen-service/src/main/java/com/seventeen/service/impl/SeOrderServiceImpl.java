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

import com.seventeen.pay.wx.service.WxPay;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.MyConfig;
import com.seventeen.service.LockService;
import com.seventeen.service.SeApartmentService;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.MyTimer;
import com.seventeen.util.PageInfo;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Autowired
    private SeApartmentCleanMapper seApartmentCleanMapper;
    @Autowired
    private LockService lockService;
    @Autowired
    private SeOrderPayMapper seOrderPayMapper;
    @Autowired
    private SeApartmentService seApartmentService;
    @Autowired
    private SeOrderService seOrderService;


    @Autowired
    private WxPay wxPay;

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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (orderInfo.getRoomId().equals("")) {
            LocalDate now = LocalDate.now();
            Result orderDate = seOrderService.getOrderDate(orderInfo.getRoomType(), sysUser);
            ArrayList<String> orderDates = (ArrayList<String>) orderDate.getData();
//            orderDates.contains()
            String[] planTime = orderInfo.getPlanTime().split("~");

            String dates = LocalDate.now().format(dateTimeFormatter2);
            String inttime = dates + " " + planTime[0].trim() + ":00";
            String ourtime = dates + " " + planTime[1].trim() + ":00";

            Result apartmentByTime = seApartmentService.getApartmentByTime(inttime, ourtime, orderInfo.getRoomType());
            ArrayList<String> data = (ArrayList<String>) apartmentByTime.getData();
            orderInfo.setRoomId(data.get(0));
//            System.out.println(apartmentByTime);
            String[] split = orderInfo.getPlanTime().split("~");
//            String[] split1 = split[0].split(":");
//            String[] split2 = split[1].split(":");
            orderInfo.setStartTime(now + " " + split[0].trim() + ":00");
            orderInfo.setEndTime(now + " " + split[1].trim() + ":00");

            Calendar instance = Calendar.getInstance();
            instance.setTime(DateUtil.parseDateAndToDate(ourtime, DateUtil.DEFAULT_DATETIME_PATTERN));
            MyTimer timer = new MyTimer();
            timer.schedule(() -> {
                this.checkOut(ourtime);
                timer.cancel();
            }, instance.getTime());
        }


//        System.out.println("订单号:" + orderId);
//        System.out.println(LocalDateTime.now().format(dateTimeFormatter));

        Result result = new Result();
        SeOrderPay orderPay = new SeOrderPay();
        orderPay.setId(orderId);
        orderPay.setCreatTime(LocalDateTime.now().format(dateTimeFormatter));
        orderPay.setStatus(0);


        if (!orderInfo.getRoomNum().equals("1")) {//多间时候需要拆分订单

            String[] idArr = orderInfo.getRoomId().split(",");
            for (String rid : idArr) {

                SeOrder se = new SeOrder();
                se.setId(IDGenerator.getId());
                se.setApId(rid);
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
                se.setLockPwd(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));

                seOrderMapper.insert(se);

                orderPay.setSeOrderId(se.getId());
                seOrderPayMapper.insert(orderPay);

                SeOrderLiver ol = new SeOrderLiver();
                ol.setId(IDGenerator.getId());
                ol.setOrderId(se.getId());
                ol.setCreateBy(sysUser.getId());
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


        } else {
            SeOrder se = new SeOrder();
            se.setId(IDGenerator.getId());
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
            se.setLockPwd(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));

            seOrderMapper.insert(se);


            orderPay.setSeOrderId(se.getId());
            seOrderPayMapper.insert(orderPay);

            SeOrderLiver ol = new SeOrderLiver();
            ol.setId(IDGenerator.getId());
            ol.setOrderId(se.getId());
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

        setOrderCalendarAdd(orderId);


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
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SeOrderPay sep = new SeOrderPay();
        sep.setId(orderId);
        List<SeOrderPay> slist = seOrderPayMapper.select(sep);
        for (SeOrderPay orderPay : slist) {
            orderPay.setStatus(1);
            orderPay.setPayTime(LocalDateTime.now().format(dateTimeFormatter));
            orderPay.setUpdateTime(LocalDateTime.now().format(dateTimeFormatter));
            seOrderPayMapper.updateByPrimaryKey(orderPay);


            SeOrder order = new SeOrder();
            order.setId(orderPay.getSeOrderId());
            order = seOrderMapper.selectOne(order);
            order.setStatus("1");
            seOrderMapper.updateByPrimaryKey(order);
        }


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

//    @Override
//    public Result getOrderDateByTime(String roomType) {
//        Result result = new Result();
//        try {
//            ArrayList seApartments = seApartmentMapper.getCanUseApartments(roomType);
//            seApartments.size();
//            SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
//            seOrderCalendar.setRoomTypeId(roomType);
//            seOrderCalendar.setOrders(seApartments.size());
//            List<String> seOrderCalendars = seOrderCalendarMapper.getOrderDate(seOrderCalendar);
//            ArrayList<String> strings = new ArrayList<>();
//            result.setData(strings);
//        } catch (Exception e) {
//            logger.error("e", e);
//            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//        return result;
//    }

    @Transactional
    @Override
    public Result cancelOrder(String order) {
        Result result = new Result();
        try {
            SeOrderPay so = new SeOrderPay();
            so.setSeOrderId(order);
            SeOrderPay orderPay = seOrderPayMapper.selectOne(so);
            String result_code = "SUCCESS";
//            String result_code = wxPay.cancelOrder(orderPay.getId());
            if (result_code.equals("SUCCESS")) {
                seOrderPayMapper.updateCancel(orderPay.getId());

                SeOrderPay so2 = new SeOrderPay();
                so2.setId(orderPay.getId());
                List<SeOrderPay> select = seOrderPayMapper.select(so2);

                for (SeOrderPay seOrderPay : select) {
                    SeOrder seOrder = new SeOrder();
                    seOrder.setId(seOrderPay.getSeOrderId());
                    SeOrder seOrder1 = seOrderMapper.selectByPrimaryKey(seOrder);
                    seOrder1.setStatus("4");
                    seOrderMapper.updateByPrimaryKeySelective(seOrder1);

                }
                setOrderCalendarReduce(orderPay.getId());
            } else {
                throw new Exception("退款失败");
            }

        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void setOrderCalendarReduce(String payId) {
        List<SeOrder> seOrders = seOrderPayMapper.getOrder(payId);
        for (SeOrder seOrder : seOrders) {
            List<String> list = new ArrayList();
            List<SeOrderCalendar> addList = new ArrayList();
            /**
             * 房间日历表-1
             */
            String apId = seOrder.getApId();
            SeApartment seApartment = new SeApartment();
            seApartment.setId(apId);
            seApartment = seApartmentMapper.selectByPrimaryKey(seApartment);

            String inTime = seOrder.getInTime().replace(".0", "");
            String outTime = seOrder.getOutTime().replace(".0", "");

            LocalDate startDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(inTime, DateUtil.DEFAULT_DATETIME_PATTERN));
            LocalDate endDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(outTime, DateUtil.DEFAULT_DATETIME_PATTERN));

            long distance = ChronoUnit.DAYS.between(startDate, endDate);
            if (distance >= 1) {
                Stream.iterate(startDate, d -> {
                    return d.plusDays(1);
                }).limit(distance + 1).forEach(f -> {
                    list.add(f.toString());
                });
                for (int i = 0; i < list.size() - 1; i++) {
                    String date = list.get(i);
                    SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                    String[] dateArr = date.split("-");
                    seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                    seOrderCalendar.setYear(dateArr[0]);
                    seOrderCalendar.setMonth(dateArr[1]);
                    seOrderCalendar.setDay(dateArr[2]);

                    seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                    seOrderCalendar.setOrders(seOrderCalendar.getOrders() - 1 < 0 ? 0 : seOrderCalendar.getOrders() - 1);
                    seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);
                }
            } else {
                SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                String time = DateUtil.format(startDate, DateUtil.DEFAULT_DATE_PATTERN);
                String[] dateArr = time.split("-");
                seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                seOrderCalendar.setYear(dateArr[0]);
                seOrderCalendar.setMonth(dateArr[1]);
                seOrderCalendar.setDay(dateArr[2]);

                seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                seOrderCalendar.setOrders(seOrderCalendar.getOrders() - 1 < 0 ? 0 : seOrderCalendar.getOrders() - 1);
                seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);
            }
        }
    }


    /**
     * 时租房离店后 日历表-1
     *
     * @param payId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setOutTimeCalendarReduce(String payId) {
        List<SeOrder> seOrders = seOrderPayMapper.getOrder(payId);
        for (SeOrder seOrder : seOrders) {
            String priceTagId = seOrder.getPriceTagId();
            SeTag seTag = new SeTag();
            seTag.setId(priceTagId);
            SeTag seTag1 = seTagMapper.selectByPrimaryKey(seTag);
            if (seTag1.getName().contains("时租房")) {

                String outTime = seOrder.getOutTime().replace(".0", "");
                Calendar instance = Calendar.getInstance();
                instance.setTime(DateUtil.parseDateAndToDate(outTime, DateUtil.DEFAULT_DATETIME_PATTERN));

                MyTimer timer = new MyTimer();
                timer.schedule(() -> {

                    /**
                     * 房间日历表-1
                     */
                    String apId = seOrder.getApId();
                    SeApartment seApartment = new SeApartment();
                    seApartment.setId(apId);
                    seApartment = seApartmentMapper.selectByPrimaryKey(seApartment);
                    String inTime = seOrder.getInTime().replace(".0", "");
                    LocalDate startDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(inTime, DateUtil.DEFAULT_DATETIME_PATTERN));
                    SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                    String time = DateUtil.format(startDate, DateUtil.DEFAULT_DATE_PATTERN);
                    String[] dateArr = time.split("-");
                    seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                    seOrderCalendar.setYear(dateArr[0]);
                    seOrderCalendar.setMonth(dateArr[1]);
                    seOrderCalendar.setDay(dateArr[2]);
                    seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                    seOrderCalendar.setOrders(seOrderCalendar.getOrders() - 1 < 0 ? 0 : seOrderCalendar.getOrders() - 1);
                    seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);

                    timer.cancel();
                }, instance.getTime());

            } else {
                return;
            }

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void setOrderCalendarAdd(String payId) {
        List<SeOrder> seOrders = seOrderPayMapper.getOrder(payId);
        for (SeOrder seOrder : seOrders) {
            List<String> list = new ArrayList();
            List<SeOrderCalendar> addList = new ArrayList();
            /**
             * 房间日历表+1
             */
            String apId = seOrder.getApId();
            SeApartment seApartment = new SeApartment();
            seApartment.setId(apId);
            seApartment = seApartmentMapper.selectByPrimaryKey(seApartment);

            String inTime = seOrder.getInTime().replace(".0", "");
            String outTime = seOrder.getOutTime().replace(".0", "");
            LocalDate startDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(inTime, DateUtil.DEFAULT_DATETIME_PATTERN));
            LocalDate endDate = DateUtil.toLocalDate(DateUtil.parseDateAndToDate(outTime, DateUtil.DEFAULT_DATETIME_PATTERN));

            long distance = ChronoUnit.DAYS.between(startDate, endDate);
            if (distance >= 1) {
                Stream.iterate(startDate, d -> {
                    return d.plusDays(1);
                }).limit(distance + 1).forEach(f -> {
                    list.add(f.toString());
                });
                for (int i = 0; i < list.size() - 1; i++) {
                    String date = list.get(i);
                    SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                    String[] dateArr = date.split("-");
                    seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                    seOrderCalendar.setYear(dateArr[0]);
                    seOrderCalendar.setMonth(dateArr[1]);
                    seOrderCalendar.setDay(dateArr[2]);

                    seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                    seOrderCalendar.setOrders(seOrderCalendar.getOrders() + 1);
                    seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);
                }
            } else {
                SeOrderCalendar seOrderCalendar = new SeOrderCalendar();
                String time = DateUtil.format(startDate, DateUtil.DEFAULT_DATE_PATTERN);
                String[] dateArr = time.split("-");
                seOrderCalendar.setRoomTypeId(seApartment.getRoomType());
                seOrderCalendar.setYear(dateArr[0]);
                seOrderCalendar.setMonth(dateArr[1]);
                seOrderCalendar.setDay(dateArr[2]);

                seOrderCalendar = seOrderCalendarMapper.selectOne(seOrderCalendar);
                seOrderCalendar.setOrders(seOrderCalendar.getOrders() + 1);
                seOrderCalendarMapper.updateByPrimaryKeySelective(seOrderCalendar);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result cancelOrderConfirm(String order) {
        Result result = new Result();
        try {
            List<String> list = new ArrayList();
            List<SeOrderCalendar> addList = new ArrayList();

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

    @Transactional
    @Override
    public Result updateLockPWD(String orderId, SysUser sysUser) {
        SeOrder se = new SeOrder();
        se.setUserId(sysUser.getId());
        se.setId(orderId);
        SeOrder seOrder = seOrderMapper.selectOne(se);
        if (seOrder == null)
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, "查询不到订单");
        seOrder.setLockPwd(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));

        LocalDateTime start = LocalDateTime.of(
                Integer.valueOf(seOrder.getInTime().substring(0, 4)),
                Integer.valueOf(seOrder.getInTime().substring(5, 7)),
                Integer.valueOf(seOrder.getInTime().substring(8, 10)),
                Integer.valueOf(seOrder.getInTime().substring(11, 13)), 0, 0);
        LocalDateTime out = LocalDateTime.of(
                Integer.valueOf(seOrder.getOutTime().substring(0, 4)),
                Integer.valueOf(seOrder.getOutTime().substring(5, 7)),
                Integer.valueOf(seOrder.getOutTime().substring(8, 10)),
                Integer.valueOf(seOrder.getOutTime().substring(11, 13)), 0, 0);

        lockService.updataLockPassWord(seOrder.getApId(), start, out, Integer.valueOf(seOrder.getLockPwd()));
        seOrderMapper.updateByPrimaryKey(seOrder);
        return new Result(seOrder.getLockPwd());
    }

    @Override
    public void checkOut(String date) {
        try {
            List<SeOrder> seOrders = seOrderMapper.getCheckOut(date);
            for (SeOrder seOrder : seOrders) {
                if (seOrder != null) {
                    seOrder.setStatus("5");
                    seOrderMapper.updateByPrimaryKeySelective(seOrder);

                    String apId = seOrder.getApId();
                    SeApartment seApartment = new SeApartment();
                    seApartment.setId(apId);
                    seApartment = seApartmentMapper.selectByPrimaryKey(seApartment);
                    seApartment.setStatus("2");
                    seApartmentMapper.updateByPrimaryKeySelective(seApartment);
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    /**
     * 门禁开门激活房间锁密码
     */
    public void upgradeLockCron(String cardNo) {
        try {
            List<SeOrder> seOrders = seOrderMapper.getOrderByidCard(cardNo);
            for (SeOrder seOrder : seOrders) {
                String inTime = seOrder.getInTime();
                String outTime = seOrder.getOutTime();
                LocalDateTime startTime = DateUtil.toLocalDateTime(DateUtil.parseTimeAndToDate(inTime, DateUtil.DEFAULT_DATETIME_PATTERN));
                LocalDateTime endTime = DateUtil.toLocalDateTime(DateUtil.parseTimeAndToDate(outTime, DateUtil.DEFAULT_DATETIME_PATTERN));

                if (seOrder != null) {
                    String password = seOrder.getLockPwd();
                    lockService.updataLockPassWord(seOrder.getApId(), startTime, endTime, Integer.valueOf(password));
                }
                seOrder.setStatus("2");
                seOrder.setUpdateTime(DateUtil.now());
                seOrderMapper.updateByPrimaryKeySelective(seOrder);
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    @Override
    public void upgradeCleanRoom() {
        try {
            seApartmentCleanMapper.upgradeCleanRoom();
        } catch (Exception e) {
            logger.error("error", e);
        }
    }

    @Override
    public Result addLiver(SysUser sysUser, AddLiver addLiver) {
        Result result = new Result();
        try {
            String orderid = addLiver.getOrderid();

            for (SeOrderLiver seOrderLiver : addLiver.getAddLiver()) {
                String liver = seOrderLiver.getLiver();

                SeOrderLiver seOrderLiver1 = seOrderLiverMapper.selectByOrderIs(orderid, liver);
                if (seOrderLiver1 != null) {
                    seOrderLiver1.setIdCard(seOrderLiver.getIdCard());
                    seOrderLiver1.setLiver(seOrderLiver.getLiver());
                    seOrderLiver1.setPhone(seOrderLiver.getPhone());
                    seOrderLiverMapper.updateByPrimaryKeySelective(seOrderLiver1);
                } else {
                    seOrderLiver1 = new SeOrderLiver();
                    seOrderLiver1.setId(IDGenerator.getId());
                    seOrderLiver1.setOrderId(orderid);
                    seOrderLiver1.setCreateBy(sysUser.getId());
                    seOrderLiver1.setCreateTime(DateUtil.now());
                    seOrderLiver1.setPhone(seOrderLiver.getPhone());
                    seOrderLiver1.setLiver(seOrderLiver.getLiver());
                    seOrderLiver1.setUpdateTime(DateUtil.now());
                    seOrderLiver1.setIdCard(seOrderLiver.getIdCard());
                    seOrderLiverMapper.insert(seOrderLiver1);
                }

            }

        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

    @Override
    public Result<String> getWifi(SysUser sysUser, String orderId) {
        Result result = new Result();
        try {
            String wifi = seOrderMapper.getWifi(orderId);
            result.setData(wifi);
        } catch (Exception e) {
            logger.error("e", e);
            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return result;
    }

}
