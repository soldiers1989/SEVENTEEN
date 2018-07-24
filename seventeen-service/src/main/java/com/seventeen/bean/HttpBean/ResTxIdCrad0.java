package com.seventeen.bean.HttpBean;

import java.util.List;

/**
 * 腾讯身份证识别 正面
 */
public class ResTxIdCrad0 {
    private List<TxResult0> result_list;

    public static class TxResult0{
        private int code;
        private String message;
        private String url;
        private Data0 data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Data0 getData() {
            return data;
        }

        public void setData(Data0 data) {
            this.data = data;
        }
    }

    public static class Data0{
        private String name;
        private String sex;
        private String nation;
        private String birth;
        private String address;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


    public List<TxResult0> getResult_list() {
        return result_list;
    }

    public void setResult_list(List<TxResult0> result_list) {
        this.result_list = result_list;
    }
}
