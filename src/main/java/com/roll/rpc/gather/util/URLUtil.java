package com.roll.rpc.gather.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.roll.rpc.gather.common.RegisterConstant.LOCAL_NETTY_SERVER_ADDRESS;

/**
 * URL 工具类
 * <p>created on 2020/7/9 3:48 下午
 */
public class URLUtil {
    /**
     * 获取本机ip
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return LOCAL_NETTY_SERVER_ADDRESS;
    }
}
