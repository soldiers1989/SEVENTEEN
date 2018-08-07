package com.seventeen.service.impl;

import com.seventeen.bean.SeUserVip;
import com.seventeen.core.Result;
import com.seventeen.mapper.SeOrderMapper;
import com.seventeen.mapper.SeUserVipMapper;
import com.seventeen.service.SeUserVipService;
import com.seventeen.util.DateUtil;
import com.seventeen.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class SeUserVipServiceImpl implements SeUserVipService {
    @Autowired
    private SeUserVipMapper seUserVipMapper;




    @Transactional
    @Override
    public Result<SeUserVip> getUserVipInfo(String userId) {
        Result<SeUserVip> result=new Result<SeUserVip>();
        LocalDate now = LocalDate.now();
        SeUserVip se=new SeUserVip();
        se.setUserid(userId);
        se.setStatus(1);
        SeUserVip select = seUserVipMapper.selectOne(se);
        if(select==null){
            se.setId(IDGenerator.getId());
            se.setStratdate(DateUtil.toDate(now));
            se.setLevel("1");
            seUserVipMapper.insert(se);
            select=se;
        }else{
            if(now.isAfter(DateUtil.toLocalDate(select.getStratdate())))//判断保级情况 --当前日期是否 需要进行保级
                switch (select.getLevel()){
                    case "1": //等级1不处理
                        break;
                    case "2": //等级2
                        if(select.getRoomNum()>=8){ //剩余房间数 大于等于8 //更新有效期,房数-8 -保级
                            select.setStratdate(DateUtil.toDate(now));
                            select.setRoomNum(select.getRoomNum()-8);
                        }else{  //降级 保留房数 --刷新更新时间
                            select.setLevel("1");
                            select.setDiscount("1");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                    case "3": //等级3
                        if(select.getRoomNum()>=20){ //剩余房间数 大于等于8 //更新有效期,房数-8 -保级
                            select.setStratdate(DateUtil.toDate(now));
                            select.setRoomNum(select.getRoomNum()-20);
                        }else{  //降级 保留房数 --刷新更新时间
                            select.setLevel("2");
                            select.setDiscount("0.95");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                    case "4": //等级4
                        if(select.getRoomNum()>=35){ //剩余房间数 大于等于8 //更新有效期,房数-8 -保级
                            select.setStratdate(DateUtil.toDate(now));
                            select.setRoomNum(select.getRoomNum()-35);
                        }else{  //降级 保留房数 --刷新更新时间
                            select.setLevel("3");
                            select.setDiscount("0.90");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                }
            else
                switch (select.getLevel()){//升级
                    case "1": //等级1
                        if(select.getRoomNum()>=10){
                            select.setLevel("2");
                            select.setRoomNum(0);
                            select.setDiscount("0.95");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                    case "2": //等级2
                        if(select.getRoomNum()>=30){
                            select.setLevel("3");
                            select.setRoomNum(0);
                            select.setDiscount("0.90");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                    case "3": //等级3
                        if(select.getRoomNum()>=40){
                            select.setLevel("4");
                            select.setRoomNum(0);
                            select.setDiscount("0.88");
                            select.setStratdate(DateUtil.toDate(now));
                        }
                    case "4": //等级4
                        break;
                }
            seUserVipMapper.updateByPrimaryKey(select);
        }
        return result.setData(select);
    }


    /**
     * 订单完成后进行房间数增加
     * @param userId
     */
    @Override
    public void addRoomCount(String userId) {
        SeUserVip se=new SeUserVip();
        se.setUserid(userId);
        se.setStatus(1);
        SeUserVip select = seUserVipMapper.selectOne(se);
        if(select==null){
            select=new SeUserVip();
            select.setId(IDGenerator.getId());
            select.setUserid(userId);
            select.setRoomNum(1);
            select.setStratdate(DateUtil.toDate(LocalDate.now()));
            seUserVipMapper.insert(select);
        }else {
            select.setRoomNum(select.getRoomNum() + 1);
            seUserVipMapper.updateByPrimaryKey(select);
        }
    }
}
