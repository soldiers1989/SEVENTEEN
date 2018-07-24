package com.seventeen.bean.HttpBean;

public class ResTxFace {
//    data.session_id	string	相应请求的 session 标识符
//    data.similarity	float	用户上传的图像与身份证登记照的人脸相似度，取值范围[0,100]，推荐相似度大于 75 时可判断为同一人，可根据具体场景自行调整阈值
//    code	int	错误码
//    message	string	错误描述

    private int code;
    private String message;
    private Datax data;
    public static class Datax{
        private float similarity;
        private String session_id;

        public float getSimilarity() {
            return similarity;
        }

        public void setSimilarity(float similarity) {
            this.similarity = similarity;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }
    }

    public Datax getData() {
        return data;
    }

    public void setData(Datax data) {
        this.data = data;
    }

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
}
