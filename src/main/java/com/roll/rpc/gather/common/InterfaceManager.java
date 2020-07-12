package com.roll.rpc.gather.common;

import com.roll.rpc.gather.client.dto.RpcClientConfig;
import com.roll.rpc.gather.client.netty.NettyClientManager;
import com.roll.rpc.gather.register.client.Register;
import com.roll.rpc.gather.register.client.RegisterService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接口管理
 * <p>created on 2020/7/11 4:41 下午
 */
@Service
public class InterfaceManager {

    private final RegisterService registerService;

    public InterfaceManager(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * 接口 对应的netty链接
     */
    private static final Map<String, ChannelFuture> nettyClientChannelFutureMap = new ConcurrentHashMap<>();

    /**
     * 接口 对应的客户端配置信息
     */
    private static final Map<String, RpcClientConfig> rpcClientConfigMap = new ConcurrentHashMap<>();

    /**
     * 全限定类名与注册信息对应关系
     */
    private static final Map<String, Register> registerMap = new ConcurrentHashMap<>();

    /**
     * 代理实例与接口的对应关系
     */
    private static final Map<String, String> proxyRegisterMap = new ConcurrentHashMap<>();

    /**
     * 配置RpcClient配置信息
     * <p>
     * 客户端启动BootStrap
     *
     * @param interfaceName   全限定类名
     * @param rpcClientConfig 客户端配置信息
     */
    public void setRpcClientConfigMap(String interfaceName, RpcClientConfig rpcClientConfig) {
        rpcClientConfigMap.put(interfaceName, rpcClientConfig);

        NettyClientManager nettyClientManager = new NettyClientManager();
        Bootstrap bootstrap = nettyClientManager.getBootstrap();

        Register register = new Register();
        register.setServiceName(interfaceName);
        register.setServiceVersion(rpcClientConfig.getVersion());
        // todo 根据注册接口的失效和新增，动态增加节点
        List<String> ipAndPortList = registerService.lookup(register);
        String url = ipAndPortList.get(0);
        String ip = url.substring(0, url.indexOf(":"));
        String port = url.substring(url.indexOf(":") + 1);
        ChannelFuture channelFuture = bootstrap.connect(ip, Integer.parseInt(port));
        nettyClientChannelFutureMap.put(interfaceName, channelFuture);
    }

    /**
     * 注册接口配置信息
     *
     * @param interfaceName 全限定类名
     * @param register      服务端配置信息
     */
    public void setRegisterMap(String interfaceName, Register register) {
        registerMap.put(interfaceName, register);
    }

    public void setProxyRegisterMap(String proxyClassName, String interfaceName) {
        proxyRegisterMap.put(proxyClassName, interfaceName);
    }

    public ChannelFuture getNettyClientChannelFuture(String interfaceName) {
        return nettyClientChannelFutureMap.get(interfaceName);
    }

    public RpcClientConfig getRpcClientConfigMap(String interfaceName) {
        return rpcClientConfigMap.get(interfaceName);
    }

    public Register getRegisterMap(String interfaceName) {
        return registerMap.get(interfaceName);
    }

    public String getInterfaceName(String proxyName) {
        return proxyRegisterMap.get(proxyName);
    }
}
