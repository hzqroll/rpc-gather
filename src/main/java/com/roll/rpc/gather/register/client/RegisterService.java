package com.roll.rpc.gather.register.client;

import java.util.List;

/**
 * 服务注册与发现接口
 * <p>
 * zk保存了信息，服务端启动的netty 服务地址和端口
 * 根路径/应用名称/全限定类名:版本号/ip:port -> ip:port
 *
 * @author roll
 * created on 2019-09-05 14:40
 */
public interface RegisterService {
    /**
     * 初始化注册器
     *
     * @param appName 应用名称
     */
    void initRegister(String appName) throws Exception;

    /**
     * 注册服务到zk上
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 是否注册成功
     */
    boolean register(Register register);

    /**
     * 取消注册到zk上
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 是否取消注册成功
     */
    boolean unRegister(Register register);

    /**
     * 在zk上寻找注册的ip+port
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 返回寻找到的ip+port
     */
    List<String> lookup(Register register);

    /**
     * 保存服务端地址
     *
     * @param appName 应用名称
     * @param url     地址：ip:port
     */
    void registerServerAddress(String appName, String url);

    /**
     * 获取服务端绑定地址
     *
     * @param appName 应用名称
     * @return 地址列表
     */
    List<String> getServerAddress(String appName);
}
