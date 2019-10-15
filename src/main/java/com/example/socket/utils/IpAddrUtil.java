package com.example.socket.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/***
 *
 * @author Gimi
 * @date 2019/10/15 20:32
 *
 ***/
public class IpAddrUtil {

    public static String getAddressByIp(String ip) {
        String address = null;
        if ("127.0.0.1".equals(ip)||"localhost".equals(ip)){
            address = "本地";
        }else {
            try {
                address = BaiduPushUtil.getAddress(ip,BaiduPushUtil.BAIDU_APK);
            }catch (Exception e){
                address =  "获取失败";
            }
        }
        return address;
    }

    public static String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        return checkIp(ip) ? ip : (
                checkIp(ip = request.getHeader("Proxy-Client-IP")) ? ip : (
                        checkIp(ip = request.getHeader("WL-Proxy-Client-IP")) ? ip :
                                request.getRemoteAddr()));
    }

    /**
     * 校验IP
     *
     * @param ip
     * @return
     */
    private static boolean checkIp(String ip) {
        return !StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }
}
