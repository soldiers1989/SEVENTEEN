package com.seventeen.service.impl;

import com.alibaba.fastjson.JSON;

import com.seventeen.bean.HttpBean.ResTxFace;
import com.seventeen.bean.HttpBean.ResTxIdCard;
import com.seventeen.bean.HttpBean.ResTxIdCrad0;
import com.seventeen.bean.HttpBean.ResTxIdCrad1;
import com.seventeen.bean.SeUserAttestation;
import com.seventeen.bean.core.SysUser;
import com.seventeen.common.utils.TxSingUtil;
import com.seventeen.core.Result;
import com.seventeen.mapper.SeUserAttestationMapper;
import com.seventeen.service.UserVerify;
import com.seventeen.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 用户实名验证类
 */
@Service
public class UserVerifyImpl implements UserVerify {

    private final Logger logger = LoggerFactory.getLogger(UserVerify.class);
    @Autowired
    private SeUserAttestationMapper seUserAttestationMapper;

    @Value("${file.upload.rootImgurl}")
    private String rootImgurl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result verify(SysUser sysUser) {

        Result result = new Result();
        SeUserAttestation se = new SeUserAttestation();
        se.setUserId(sysUser.getId());
        SeUserAttestation seUserAttestation = seUserAttestationMapper.selectOne(se);
        if (seUserAttestation == null) {
            result.setData("用户还没有上传图片");
            result.setResultCode(4001);
        }
        //识别身份证上的信息
        try {
            String idCradMsg = TxSingUtil.getIdCradMsg( "https://www.17inn.com/seventeen/"+ seUserAttestation.getIdcard0(), 0);
            ResTxIdCrad0 idCrad0 = JSON.parseObject(idCradMsg, ResTxIdCrad0.class);
            if (idCrad0 == null)
                throw new Exception("识别idCrad0出错");
            if (idCrad0.getResult_list().size() > 0) {
                if (idCrad0.getResult_list().get(0).getCode() == 0) {
                    seUserAttestation.setTrueName(idCrad0.getResult_list().get(0).getData().getName());
                    seUserAttestation.setRecTime(DateUtil.toDate(LocalDateTime.now()));
                    seUserAttestation.setIdCode(idCrad0.getResult_list().get(0).getData().getId());
                    seUserAttestation.setAddress(idCrad0.getResult_list().get(0).getData().getAddress());
                    seUserAttestation.setSex(idCrad0.getResult_list().get(0).getData().getSex());
                    seUserAttestation.setNation(idCrad0.getResult_list().get(0).getData().getNation());
                    seUserAttestation.setBirth(idCrad0.getResult_list().get(0).getData().getBirth());

                    //获取背面
                    String idCrad1Msg = TxSingUtil.getIdCradMsg("https://www.17inn.com/seventeen/"+seUserAttestation.getIdcard1(), 1);
                    System.out.println(idCrad1Msg);
                    ResTxIdCrad1 idCrad1 = JSON.parseObject(idCrad1Msg, ResTxIdCrad1.class);
                    if (idCrad1 == null)
                        throw new Exception("识别idCrad1出错");
                    if (idCrad1.getResult_list().size() > 0) {
                        if (idCrad1.getResult_list().get(0).getCode() == 0) {
                            seUserAttestation.setAuthority(idCrad1.getResult_list().get(0).getData().getAuthority());
                            String[] split = idCrad1.getResult_list().get(0).getData().getValid_date().split("-")[1].replace(".", "-").split("-");
                            seUserAttestation.setEndTime(DateUtil.toDate(LocalDate.of(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]))));
                        }
                    }
                    //身份证可信度
                    String s = TxSingUtil.verifyIdCrad(seUserAttestation.getTrueName(), seUserAttestation.getIdCode());
                    ResTxIdCard idCradVerify = JSON.parseObject(s, ResTxIdCard.class);
                    if (idCradVerify != null) {
                         /*
                            -5803	身份证号码与姓名不匹配
                            -5804	身份证号码无效
                            -5806	身份证号码或者姓名格式错误
                            -5807	查询身份证信息失败
                         */
                        seUserAttestation.setVerifyIdCrad(idCradVerify.getCode());
                        seUserAttestation.setVerifyIdCradDesc(idCradVerify.getMessage());


                        //人脸识别
                        if(idCradVerify.getCode()==0){
                            String resString=TxSingUtil.faceIdCardCompare("",seUserAttestation.getTrueName(),seUserAttestation.getIdCode(),rootImgurl+seUserAttestation.getUserPhoto());
//                            String resString=TxSingUtil.faceIdCardCompare("",seUserAttestation.getTrueName(),seUserAttestation.getIdCode(),rootImgurl+seUserAttestation.getUserPhoto());

//                            String resString=TxSingUtil.verifyFace(seUserAttestation.getTrueName(), seUserAttestation.getIdCode(),"https://www.17inn.com/seventeen/"+seUserAttestation.getUserPhoto());
                            ResTxFace face = JSON.parseObject(resString, ResTxFace.class);
                            if(face!=null){
                                seUserAttestation.setFaceCode(face.getCode());
                                seUserAttestation.setFaceDesc(face.getMessage());
                                seUserAttestation.setSimilarity(face.getData().getSimilarity());
                                if(face.getData().getSimilarity()>=75){
                                    seUserAttestation.setIsPass(1);
                                    result.setData("验证成功");
                                }else{
                                    seUserAttestation.setIsPass(2);
                                    result.setData("人脸识别认证失败,请换一张清晰的照片");
                                }
                            }
                        }else{
                            result.setData(idCradVerify.getMessage());
                        }
                    }

                }
                seUserAttestationMapper.updateByPrimaryKeySelective(seUserAttestation);
            } else {
                throw new Exception("识别idCrad0长度为0");
            }
        } catch (Exception e) {
            logger.error("验证出错", e);
        }
        return result;
    }

    @Override
    public Result isVerify(SysUser sysUser) {
        Result result = new Result();
        HashMap<String,Object> map=new HashMap<>();
        map.put("isVerify",false);
        map.put("tName","");
        SeUserAttestation se = new SeUserAttestation();
        se.setUserId(sysUser.getId());
        SeUserAttestation seUserAttestation = seUserAttestationMapper.selectOne(se);
        if(seUserAttestation!=null){
            map.put("tName",seUserAttestation.getTrueName());
            if(seUserAttestation!=null&&seUserAttestation.getIsPass()==1)
            map.put("isVerify",true);
        }
        result.setData(map);
        return result;
    }
}
