package com.roll.rpc.gather.common;

/**
 * @author roll
 * created on 2019-09-05 15:48
 */
public class RegisterConstant {
    /**
     * 注册服务根路径
     */
    public static final String SERVICE_ROOT = "/register";

    /**
     * zk session超时时间
     */
    public static final int ZK_SESSION_TIMEOUAT = 10000;

    /**
     * zk 链接超时时间
     */
    public static final int ZK_CONNECT_TIMEOUT = 10000;

    public static final String LOCAL_NETTY_SERVER_ADDRESS = "127.0.0.1";

    public static final String LOCAL_NETTY_SERVER_PORT = "8001";
}
