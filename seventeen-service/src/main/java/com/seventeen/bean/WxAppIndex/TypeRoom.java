package com.seventeen.bean.WxAppIndex;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 房间类型
 */
@Data
public class TypeRoom {

    /** 房间类型*/
    private String  roomType;
    /** 房间类型名字*/
    private String  name;
    /** 房间价格*/
    private String  price;
    /** 房间面积*/
    private String  area;
    /** 房间描述*/
    private String remark;
    /** 房间创建时间*/
    private String  createTime;
    /** 图片路径*/
    private String  imgUrl;


}
