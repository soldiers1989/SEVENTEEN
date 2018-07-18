package com.seventeen.pay.wx.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.seventeen.common.utils.HttpUtil;
import com.seventeen.pay.wx.bean.WxOrderBean;
import com.seventeen.pay.wx.service.WxPay;
import com.seventeen.pay.wx.util.PayCommonUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class WxPayImpl implements WxPay {
    private String total_fee;//总金额
    private String body = "EXia";//商品描述
    private String detail;//商品详情
    private String attach;//附加数据
    private String time_start;//交易起始时间
    private String time_expire;//交易结束时间
    private String openid= "oBixG4yTazP6-fwDNZQtf5MObxAo";//用户标识
    private JSONArray jsonArray = new JSONArray();

    public static void main(String[] args) {
        WxPayImpl wx = new WxPayImpl();

            wx.wxPay();

    }

    @Override
    public String creatOrder(WxOrderBean orderBean) throws UnsupportedEncodingException {
//
        return null;
    }



    /**
     * @Description: 发起微信支付
     */
    public Json wxPay() {
        String nonce_str = RandomStringUtils.randomAlphanumeric(32);

        //时间戳
        String times = System.currentTimeMillis() + "";

        Random random = new Random();
        String did = times+random.nextInt(1000);

        /**
         * <appid>wx2421b1c4370ec43b</appid>
         <>支付测试</attach>
         <body></body>
         <>10000100</mch_id>
         <detail><![CDATA[{ "goods_detail":[ { "goods_id":"iphone6s_16G", "wxpay_goods_id":"1001", "goods_name":"iPhone6s 16G", "quantity":1, "price":528800, "goods_category":"123456", "body":"苹果手机" }, { "goods_id":"iphone6s_32G", "wxpay_goods_id":"1002", "goods_name":"iPhone6s 32G", "quantity":1, "price":608800, "goods_category":"123789", "body":"苹果手机" } ] }]]></detail>
         <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
         <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>
         <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
         <out_trade_no>1415659990</out_trade_no>
         <spbill_create_ip>14.23.150.211</spbill_create_ip>
         <total_fee>1</total_fee>
         <trade_type>JSAPI</trade_type>
         <sign>0CB01533B8C1EF103065174F50BCA001</sign>
         */


        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", "wxe6f9450a01336cb9");//微信小程序ID
        packageParams.put("attach", "JSAPI支付测试");//微信小程序ID
        packageParams.put("mch_id", "1509646201");//商户ID
        packageParams.put("nonce_str", nonce_str);//随机字符串（32位以内） 这里使用时间戳
        packageParams.put("body", "测试");//支付主体名称 自定义
        packageParams.put("out_trade_no", did+"ACX");//编号 自定义以时间戳+随机数+商品ID
        packageParams.put("total_fee", "1");//价格 自定义
        packageParams.put("spbill_create_ip", "39.107.111.100");
        packageParams.put("notify_url", "https://www.17inn.com/order/wx/payCallback");//支付返回地址要外网访问的到， localhost不行，调用下面buy方法。（订单存入数据库）
        packageParams.put("trade_type", "JSAPI");//这个api有，固定的
        packageParams.put("openid", openid);//用户的openid 可以要 可以不要`
        //获取sign
        String sign = PayCommonUtil.createSign("UTF-8", packageParams, "tWe8yq3SVen03rkrOu4VAlDs8UiEsQKD");//最后这个是自己在微信商户设置的32位密钥


        packageParams.put("sign", sign);

        boolean sign1 = PayCommonUtil.isTenpaySign("UTF-8", packageParams, "tWe8yq3SVen03rkrOu4VAlDs8UiEsQKD");//最后这个是自己在微信商户设置的32位密钥
        System.out.println(sign1);

        System.out.println(sign);
        //转成XML
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        //得到含有prepay_id的XML
        String resXml = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", requestXML);
        System.out.println(resXml);



//        String appid = "wxe6f9450a01336cb9";//小程序ID
//        String mch_id = "1509646201";//商户号
//        String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String code = "1232";
//        String out_trade_no = mch_id + today + code;//商户订单号
//        String spbill_create_ip = "39.107.111.100";//终端IP
//        String notify_url = "https://www.17inn.com/";//通知地址
//        String trade_type = "JSAPI";//交易类型
//        openid = "oBixG4yTazP6-fwDNZQtf5MObxAo";//用户标识
//        try {
//            //生成的随机字符串
//            String nonce_str = RandomStringUtils.randomAlphanumeric(32);
//            //商品名称
//            String body = "测试商品名称";
//            //获取客户端的ip地址
////            String spbill_create_ip = getIpAddr(request);
//
//            //组装参数，用户生成统一下单接口的签名
//            Map<String, String> packageParams = new HashMap<String, String>();
//            packageParams.put("appid", "wxe6f9450a01336cb9");
//            packageParams.put("mch_id", "1509646201");
//            packageParams.put("nonce_str", nonce_str);
//            packageParams.put("body", "123");
//            packageParams.put("out_trade_no", out_trade_no);//商户订单号
//            packageParams.put("total_fee", "1");//支付金额，这边需要转成字符串类型，否则后面的签名会失败
//            packageParams.put("spbill_create_ip", spbill_create_ip);
//            packageParams.put("notify_url", notify_url);//支付成功后的回调地址
//            packageParams.put("trade_type", trade_type);//支付方式
//            packageParams.put("openid", openid);
//
//
//            String xml = WXPayUtil.generateSignedXml(packageParams,"tWe8yq3SVen03rkrOu4VAlDs8UiEsQKD");
//            String s = HttpUtil.postData("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
//
//
//            System.out.println(xml);
//
//            System.out.println("---"+s);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }


    /**
     * 方法名: getRemotePortData
     * 描述: 发送远程请求 获得代码示例
     * 参数：  @param urls 访问路径
     * 参数：  @param param 访问参数-字符串拼接格式, 例：port_d=10002&port_g=10007&country_a=
     * 创建人: Xia ZhengWei
     * 创建时间: 2017年3月6日 下午3:20:32
     * 版本号: v1.0
     * 返回类型: String
     */
    private String getRemotePortData(String urls, String param){
        System.out.println("港距查询抓取数据----开始抓取外网港距数据");
        try {
            URL url = new URL(urls);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            conn.setConnectTimeout(30000);
            // 设置读取超时时间
            conn.setReadTimeout(30000);
            conn.setRequestMethod("POST");
//            if(StringUtil.isNotBlank(param)) {
                conn.setRequestProperty("Origin", "https://sirius.searates.com");// 主要参数
                conn.setRequestProperty("Referer", "https://sirius.searates.com/cn/port?A=ChIJP1j2OhRahjURNsllbOuKc3Y&D=567&G=16959&shipment=1&container=20st&weight=1&product=0&request=&weightcargo=1&");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");// 主要参数
//            }
            // 需要输出
            conn.setDoInput(true);
            // 需要输入
            conn.setDoOutput(true);
            // 设置是否使用缓存
            conn.setUseCaches(false);
            // 设置请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");

//            if(StringUtil.isNotBlank(param)) {
                // 建立输入流，向指向的URL传入参数
                DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(param);
                dos.flush();
                dos.close();
//            }
            // 输出返回结果
            InputStream input = conn.getInputStream();
            int resLen =0;
            byte[] res = new byte[1024];
            StringBuilder sb=new StringBuilder();
            while((resLen=input.read(res))!=-1){
                sb.append(new String(res, 0, resLen));
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("港距查询抓取数据----抓取外网港距数据发生异常：" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("港距查询抓取数据----抓取外网港距数据发生异常：" + e.getMessage());
        }
        System.out.println("港距查询抓取数据----抓取外网港距数据失败, 返回空字符串");
        return "";
    }

    /**
     * IpUtils工具类方法
     * 获取真实的ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
