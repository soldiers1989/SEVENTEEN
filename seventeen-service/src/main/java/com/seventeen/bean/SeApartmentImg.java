package com.seventeen.bean;

import com.seventeen.util.FileUploadUtil;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Id;

/**
 * @Author: csk
 * @Date: 2018/5/28 16:20
 */
public class SeApartmentImg {
    @Id
    private String id;

    private String apId;

    private String url;

    private String mixUrl;

    private String master;

    private String sort;

    private String createTime;

    private String createBy;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMixUrl() {
        return mixUrl;
    }

    public void setMixUrl(String mixUrl) {
        this.mixUrl = mixUrl;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}