package com.seventeen.bean;

import javax.persistence.Id;
import java.util.Date;

public class SeUserAttestation {
    @Id
    private String id;

    private String userId;

    private String idcard0;

    private String idcard1;

    private String userPhoto;

    private String idCode;

    private String trueName;

    private Date endTime;

    private Date recTime;

    private Integer isPass;

    private String sex;

    private String nation;

    private String birth;

    private String address;

    private String authority;

    private Integer verifyIdCrad;

    private String verifyIdCradDesc;

    private Integer faceCode;

    private String faceDesc;

    private Float similarity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getIdcard0() {
        return idcard0;
    }

    public void setIdcard0(String idcard0) {
        this.idcard0 = idcard0 == null ? null : idcard0.trim();
    }

    public String getIdcard1() {
        return idcard1;
    }

    public void setIdcard1(String idcard1) {
        this.idcard1 = idcard1 == null ? null : idcard1.trim();
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto == null ? null : userPhoto.trim();
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode == null ? null : idCode.trim();
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName == null ? null : trueName.trim();
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRecTime() {
        return recTime;
    }

    public void setRecTime(Date recTime) {
        this.recTime = recTime;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth == null ? null : birth.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority == null ? null : authority.trim();
    }

    public Integer getVerifyIdCrad() {
        return verifyIdCrad;
    }

    public void setVerifyIdCrad(Integer verifyIdCrad) {
        this.verifyIdCrad = verifyIdCrad;
    }

    public String getVerifyIdCradDesc() {
        return verifyIdCradDesc;
    }

    public void setVerifyIdCradDesc(String verifyIdCradDesc) {
        this.verifyIdCradDesc = verifyIdCradDesc == null ? null : verifyIdCradDesc.trim();
    }

    public Integer getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(Integer faceCode) {
        this.faceCode = faceCode;
    }

    public String getFaceDesc() {
        return faceDesc;
    }

    public void setFaceDesc(String faceDesc) {
        this.faceDesc = faceDesc == null ? null : faceDesc.trim();
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }
}