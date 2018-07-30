package com.seventeen.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxpay.sdk.WXPay;
import com.seventeen.bean.*;
import com.seventeen.bean.OrderCenter;
import com.seventeen.bean.OrderInfo;
import com.seventeen.bean.SeApartment;
import com.seventeen.bean.SeOrder;
import com.seventeen.bean.core.SysUser;
import com.seventeen.core.Result;
import com.seventeen.core.ResultCode;
import com.seventeen.exception.ServiceException;
import com.seventeen.mapper.SeOrderLiverMapper;
import com.seventeen.mapper.SeApartmentMapper;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.mapper.SeUserAttestationMapper;
import com.seventeen.pay.wx.util.MD5;
import com.seventeen.pay.wx.util.MyConfig;
import com.seventeen.service.SeOrderService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import com.seventeen.util.PageInfo;
import javafx.scene.input.DataFormat;
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
import java.util.concurrent.*;
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

    public static void main(String[] args) {

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

            SeOrderLiver ol=new SeOrderLiver();
            ol.setId(IDGenerator.getId());
            ol.setOrderId(orderId);
            ol.setCreateBy(se.getCreateBy());
            ol.setCreateTime(se.getCreateTime());
            ol.setPhone(se.getCreatorPhone());
            ol.setLiver(se.getCreateBy());
            ol.setUpdateTime(LocalDateTime.now().format(dateTimeFormatter));
            SeUserAttestation su=new SeUserAttestation();
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

        SeOrder se=new SeOrder();
        se.setId(orderId);
        se=seOrderMapper.selectOne(se);
        se.setStatus("1");
        seOrderMapper.updateByPrimaryKey(se);
    }


    @Override
    public Result getOrderDate(String roomType, SysUser sysUser)  {
        Result result = new Result();

        SeApartment seApartment = new SeApartment();
        seApartment.setRoomType(roomType);
        List<SeApartment> seApartments = seApartmentMapper.select(seApartment);


        List<SeOrder> seOrders = seOrderMapper.getOrdersByStatus();
        int size = seOrders.size();
        if (size > 0) {
            ExecutorService executorService = Executors.newCachedThreadPool();
            final Semaphore semaphore = new Semaphore(50);
            final CountDownLatch countDownLatch = new CountDownLatch(size);
            List<List> list = Collections.synchronizedList(new ArrayList());
            for (SeOrder seOrder : seOrders) {
                executorService.execute(() -> {
                    try {
                        semaphore.acquire();
                        list.add(getOrderDateList(seOrder));
                        semaphore.release();
                    } catch (Exception e) {
                        logger.error("exception", e);
                        throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    countDownLatch.countDown();

                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("exception", e);
                throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
            }
            executorService.shutdown();

            /**
             * 所有线程完成后进行业务处理*/

            int t = 1;
            for (int i = 0; i < list.size(); i++) {
                List dateList = list.get(i);
                if(i+1<list.size()){
                    boolean flag = dateList.retainAll(list.get(i + 1));
                    if(flag==true){
                        t+=1;
                        list.add(i+1,dateList);
                    }
                }else{
                    if(t==size){
                        logger.info("不可用日期:{}",dateList);

                    }
                }
            }
        }

        return result;
    }

    private List getOrderDateList(SeOrder seOrder) {
        List list = Collections.synchronizedList(new ArrayList());
        String inTime = seOrder.getInTime();
        String outTime = seOrder.getOutTime();
        Date inDate = DateUtil.parseAndToDate(inTime, "yyyy-MM-dd HH:mm:ss");
        Date outDate = DateUtil.parseAndToDate(outTime, "yyyy-MM-dd HH:mm:ss");

        LocalDate startDate = DateUtil.toLocalDate(inDate);
        LocalDate endDate = DateUtil.toLocalDate(outDate);

        long distance = ChronoUnit.DAYS.between(startDate, endDate);
        if (distance < 1) {
            return null;
        }
        Stream.iterate(startDate, d -> {
            return d.plusDays(1);
        }).limit(distance + 1).forEach(f -> {
            list.add(f.toString());
        });
        return list;
    }
}
