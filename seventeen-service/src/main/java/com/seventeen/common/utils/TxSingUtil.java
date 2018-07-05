package com.seventeen.common.utils;

import com.alibaba.fastjson.JSON;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class TxSingUtil {
    //    public static Long AppID = 1253967925l;
//    public static String SecretID = "你自己的SecretID";
//    public static String SecretKey = "你自己的SecretKey";
//    public static String userQQ = "你自己的userQQ";
    static class uid {
        private String appid;
        private int card_type;
        private List<String> url_list;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public int getCard_type() {
            return card_type;
        }

        public void setCard_type(int card_type) {
            this.card_type = card_type;
        }

        public List<String> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<String> url_list) {
            this.url_list = url_list;
        }
    }


    public static void main(String[] args) throws Exception {
        String s = appSign(1253967925, "AKIDWBrechGEUasDcsFEIOWg2SjR3Ulr5OoI", "gVr0aMEvdChHwwzvy49CIRvIUVKCvYG3", "", 12l);
        System.out.println(s);

        uid u=new uid();
        u.setAppid("1253967925");
        u.setCard_type(1);

        List<String> slist=new ArrayList<>();
        slist.add("https://www.17inn.com/img/mys2.jpg");
        u.setUrl_list(slist);

        String s1 = JSON.toJSONString(u);
        String post = HttpUtilYoutu.post("http://recognition.image.myqcloud.com/ocr/idcard", s1, "utf-8", s,"recognition.image.myqcloud.com");
//        String post = HttpUtilYoutu.post("http://service.image.myqcloud.com/detection/porn_detect", s1, "utf-8", s,"service.image.myqcloud.com");
        System.out.println(post.trim());
    }

    /**
     * 生成 Authorization 签名字段
     *
     * @param appId
     * @param secretId
     * @param secretKey
     * @param bucketName
     * @param expired
     * @return
     * @throws Exception
     */
    public static String appSign(long appId, String secretId, String secretKey, String bucketName,
                                 long expired) throws Exception {
        long now = System.currentTimeMillis() / 1000;
        int rdm = Math.abs(new Random().nextInt());
        String plainText = String.format("a=%d&b=%s&k=%s&t=%d&e=%d&r=%d", appId, bucketName,
                secretId, now, now + expired, rdm);
        byte[] hmacDigest = HmacSha1(plainText, secretKey);
        byte[] signContent = new byte[hmacDigest.length + plainText.getBytes().length];
        System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
        System.arraycopy(plainText.getBytes(), 0, signContent, hmacDigest.length,
                plainText.getBytes().length);
        return Base64Encode(signContent);
    }

    /**
     * 生成 base64 编码
     *
     * @param binaryData
     * @return
     */
    public static String Base64Encode(byte[] binaryData) {
        String encodedstr = Base64.getEncoder().encodeToString(binaryData);
        return encodedstr;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param binaryData
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] HmacSha1Digest = mac.doFinal(binaryData);
        return HmacSha1Digest;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(String plainText, String key) throws Exception {
        return HmacSha1(plainText.getBytes(), key);
    }

}
