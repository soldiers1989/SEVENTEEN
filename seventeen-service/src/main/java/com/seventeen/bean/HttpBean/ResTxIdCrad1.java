package com.seventeen.bean.HttpBean;

import java.util.List;

/**
 * 腾讯身份证识别 正面
 */
public class ResTxIdCrad1 {
    private List<TxResult1> result_list;

    public static class TxResult1{
        private int code;
        private String message;
        private String url;
        private Data1 data;

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

        public Data1 getData() {
            return data;
        }

        public void setData(Data1 data) {
            this.data = data;
        }
    }

    public static class Data1{
        private String authority;
        private String valid_date;

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }

        public String getValid_date() {
            return valid_date;
        }

        public void setValid_date(String valid_date) {
            this.valid_date = valid_date;
        }
    }


    public List<TxResult1> getResult_list() {
        return result_list;
    }

    public void setResult_list(List<TxResult1> result_list) {
        this.result_list = result_list;
    }
}
