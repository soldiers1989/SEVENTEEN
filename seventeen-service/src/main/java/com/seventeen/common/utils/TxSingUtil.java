package com.seventeen.common.utils;

import com.alibaba.fastjson.JSON;
import com.qcloud.image.ImageClient;
import com.qcloud.image.demo.Demo;
import com.qcloud.image.request.FaceIdCardCompareRequest;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.util.*;
import java.util.logging.Level;


public class TxSingUtil {
    private static final Long appId=1257058026l;
    private static final  String secretId="AKIDIZaZYPWcyP1VpLErpcEffppdH0WPmjJi";
    private static final  String secretKey="PdfZLulSyIQhpsmV4MXWLRSItNgnjy63";



    static class Uid {
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

    static class IdCradInfo{
        private String appid;	//是	string	项目 ID
        private String idcard_number;	//是	string	身份证号
        private String idcard_name;	//是	string	姓名（UTF-8 编码）

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getIdcard_number() {
            return idcard_number;
        }

        public void setIdcard_number(String idcard_number) {
            this.idcard_number = idcard_number;
        }

        public String getIdcard_name() {
            return idcard_name;
        }

        public void setIdcard_name(String idcard_name) {
            this.idcard_name = idcard_name;
        }
    }

    //    appid	是	String	接入项目的唯一标识，可在 账号信息 或 云 API 密钥 中查看
//    idcard_number	是	String	用户身份证号码
//    idcard_name	是	String	用户身份证姓名 （中文，请注意使用 UTF-8 编码）
//    image	否	Binary	image 和 url 只提供一个即可
//    url	否	String	image 和 url 只提供一个即可；如果都提供，只使用 url
//    session_id	否	String	相应请求的 session 标识符，可用于结果查询

    static class FaceInfo{
        private String  appid;//	是	string	接入项目的唯一标识，可在 账号信息 或 云 API 密钥 中查看
        private String  idcard_number;//	是	string	用户身份证号码
        private String  idcard_name;//	是	string	用户身份证姓名 （中文，请注意使用 UTF-8 编码）
        private String  url;//	否	string	image 和 url 只提供一个即可；如果都提供，只使用 url
//        private String  session_id;//	否	string	相应请求的 session 标识符，可用于结果查询


        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getIdcard_number() {
            return idcard_number;
        }

        public void setIdcard_number(String idcard_number) {
            this.idcard_number = idcard_number;
        }

        public String getIdcard_name() {
            return idcard_name;
        }

        public void setIdcard_name(String idcard_name) {
            this.idcard_name = idcard_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static void main(String[] args) {

        String appId = "1257058026l";
        String secretId = "AKIDIZaZYPWcyP1VpLErpcEffppdH0WPmjJi";
        String secretKey = "PdfZLulSyIQhpsmV4MXWLRSItNgnjy63";
        String bucketName = "";
//        private static final Long appId=1257058026l;
//        private static final  String secretId="AKIDIZaZYPWcyP1VpLErpcEffppdH0WPmjJi";
//        private static final  String secretKey="PdfZLulSyIQhpsmV4MXWLRSItNgnjy63";




        faceIdCardCompare("","凌梓恒","440112199311181210","");
    }



//    public static void main(String[] args) throws Exception {
//        String s = appSign(appId, secretId, secretKey, "", 5000);
//        FaceInfo id=new FaceInfo();
//        id.setAppid(appId.toString());
//        id.setIdcard_name("凌梓恒");
//        id.setIdcard_number("440112199311181210");
////        imgUrl=imgUrl.replace("\\","/");
//        id.setUrl("https://www.17inn.com/seventeen/file/rlsb/37.jpg");
//        String s1 = JSON.toJSONString(id);
//
//
////        appid	是	String	接入项目的唯一标识，可在 账号信息 或 云 API 密钥 中查看
////        idcard_number	是	String	用户身份证号码
////        idcard_name	是	String	用户身份证姓名 （中文，请注意使用 UTF-8 编码）
////        image	否	Binary	image 和 url 只提供一个即可
////        url	否	String	image 和 url 只提供一个即可；如果都提供，只使用 url
////        session_id	否	String	相应请求的 session 标识符，可用于结果查询
//
//        Map<String,String> map=new HashMap<>();
//        map.put("appid",appId.toString());
//        map.put("idcard_number","440112199311181210");
//        map.put("idcard_name","凌梓恒");
//        map.put("url","https://www.17inn.com/seventeen/file/rlsb/37.jpg");
//
//
//        HttpUtilYoutu.sendPost("http://recognition.image.myqcloud.com/face/idcardcompare",s1,"utf-8",s,"recognition.image.myqcloud.com",map);
//
////        String post = HttpUtilYoutu.post("https://recognition.image.myqcloud.com/face/idcardcompare", s1, "utf-8", s,"recognition.image.myqcloud.com");
////        System.out.println(post.trim());
//
////
////        String s = appSign(appId, secretId, secretKey, "", 500);
////        Uid u=new Uid();
////        u.setAppid(appId.toString());
////        u.setCard_type(1);
////
////        List<String> slist=new ArrayList<>();
////        slist.add("https://www.17inn.com/seventeen/file/rlsb/20180904161505053222437.jpg");
////        u.setUrl_list(slist);
////
////        String s1 = JSON.toJSONString(u);
////        String post = HttpUtilYoutu.post("http://recognition.image.myqcloud.com/ocr/idcard", s1, "utf-8", s,"recognition.image.myqcloud.com");
//////        String post = HttpUtilYoutu.post("http://service.image.myqcloud.com/detection/porn_detect", s1, "utf-8", s,"service.image.myqcloud.com");
////
//    }


    /**
     * 获取身份证识别信息
     * @param imgsUrl
     * @param type
     * @return String - 身份证信息
     */
    public static String getIdCradMsg(String imgsUrl,int type) throws Exception {
        String s = appSign(appId, secretId, secretKey, "", 500);
        Uid u=new Uid();
        u.setAppid(appId.toString());
        u.setCard_type(type);
        List<String> slist=new ArrayList<>();
        imgsUrl=imgsUrl.replace("\\","/");
        slist.add(imgsUrl);
        u.setUrl_list(slist);
        String s1 = JSON.toJSONString(u);
        String post = HttpUtilYoutu.post("http://recognition.image.myqcloud.com/ocr/idcard", s1, "utf-8", s,"recognition.image.myqcloud.com");
        return post;
    }


    /**
     * 验证身份证可信度
     * @return String - 验证身份证可信度
     */
    public static String verifyIdCrad(String trueName,String IdCode) throws Exception {
        String s = appSign(appId, secretId, secretKey, "", 500);
        IdCradInfo id=new IdCradInfo();
        id.setAppid(appId.toString());
        id.setIdcard_name(trueName);
        id.setIdcard_number(IdCode);
        String s1 = JSON.toJSONString(id);
        String post = HttpUtilYoutu.post("http://recognition.image.myqcloud.com/auth/idcard", s1, "utf-8", s,"recognition.image.myqcloud.com");
        return post;
    }




    /**
     * 验证人脸识别匹配
     * @return String - 验证人脸识别匹配
     */
    public static String verifyFace(String trueName,String IdCode,String imgUrl) throws Exception {
        String s = appSign(appId, secretId, secretKey, "", 5000);
        FaceInfo id=new FaceInfo();
        id.setAppid(appId.toString());
        id.setIdcard_name(trueName);
        id.setIdcard_number(IdCode);
//        imgUrl=imgUrl.replace("\\","/");
        id.setUrl(imgUrl);
        String s1 = JSON.toJSONString(id);
        String post = HttpUtilYoutu.post("http://recognition.image.myqcloud.com/face/idcardcompare", s1, "utf-8", s,"recognition.image.myqcloud.com");
        return post;
    }


    /**
     * 身份证识别对比接口
     */
    public static String faceIdCardCompare( String bucketName,String trueName,String IdCode,String imgUrl ) {
        ImageClient imageClient = new ImageClient(appId.toString(), secretId, secretKey, ImageClient.NEW_DOMAIN_recognition_image_myqcloud_com/*根据文档说明选择域名*/);
        String ret;

        //2. 图片内容方式
        System.out.println("====================================================");
        String idcardCompareName = "";
        File idcardCompareImage = null;
        try {
            String[] split = imgUrl.split("/");
            idcardCompareName = split[split.length];
            idcardCompareImage = new File(imgUrl);
//
//            idcardCompareName = "37.jpg";
//            idcardCompareImage = new File("C:\\Users\\dell\\Desktop\\37.jpg");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        FaceIdCardCompareRequest idCardCompareReq = new FaceIdCardCompareRequest(bucketName,IdCode , trueName, idcardCompareName, idcardCompareImage,"");
        ret = imageClient.faceIdCardCompare(idCardCompareReq);
        System.out.println("face idCard Compare ret:" + ret);
        return ret;
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
        String plainText = String.format("a=%d&b=%s&k=%s&t=%d&e=%d&r=%d", appId, bucketName, secretId, now, now + expired, rdm);
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
