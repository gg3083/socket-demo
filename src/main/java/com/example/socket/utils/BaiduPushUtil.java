package com.example.socket.utils;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

/**
 * 百度站长推送工具类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */
@Slf4j
public class BaiduPushUtil extends RestClientUtil {

    private static final String GET_LOCATION_BY_IP = "{0}?ak={1}&coor=gcj02&ip={2}";
    private static final String BAIDU_PUSH_URL_PATTERN = "{0}{1}?site={2}&token={3}";
    public static final String BAIDU_APK="AMI5jvYpiQy97bbGxySMElmtRiZ4jHRg";
    /**
     * 通过百度API 根据ip获取定位的接口
     */
    public static final String BAIDU_API_GET_LOCATION_BY_IP = "https://api.map.baidu.com/location/ip";
    /**
     * 根据ip获取定位信息的接口地址
     *
     * @param ip
     *         用户IP
     * @return
     */
    public static String getLocationByIp(String ip, String baiduApiAk) {
        return MessageFormat.format(GET_LOCATION_BY_IP, BAIDU_API_GET_LOCATION_BY_IP, baiduApiAk, ip);
    }


    public static String getAddress(String ip,String key){

        String locationJson = RestClientUtil.get(getLocationByIp(ip, key));
        JSONObject localtionContent = JSONObject.parseObject(locationJson).getJSONObject("content");

        if (localtionContent.containsKey("address_detail")) {
            JSONObject addressDetail = localtionContent.getJSONObject("address_detail");
            String city = addressDetail.getString("city");
            String district = addressDetail.getString("district");
            String street = addressDetail.getString("street");
            String address = addressDetail.getString("province") + (StringUtils.isEmpty(city) ? "" : city) +
                    (StringUtils.isEmpty(district) ? "" : district) +
                    (StringUtils.isEmpty(street) ? "" : street);
            return address;
        }
        return "获取失败";
    }
}
