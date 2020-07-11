package com.roll.rpc.gather.server.rpcserver;

import com.roll.rpc.gather.common.InterfaceManager;
import com.roll.rpc.gather.register.client.Register;
import com.roll.rpc.gather.register.client.RegisterService;
import com.roll.rpc.gather.server.netty.NettyService;
import com.roll.rpc.gather.util.URLUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import static com.roll.rpc.gather.common.RegisterConstant.LOCAL_NETTY_SERVER_PORT;

/**
 * @author roll
 * created on 2019-09-06 09:23
 */
@Service
public class RpcServerLauncher implements ApplicationContextAware, InitializingBean {
    /**
     * 具体注册实例
     */
    private final RegisterService registerService;

    private final InterfaceManager interfaceManager;

    /**
     * 应用名
     */
    private final String appName;

    private final String nettyAddress;

    private final String nettyPort;

    private final NettyService nettyService;

    public RpcServerLauncher(RegisterService registerService, InterfaceManager interfaceManager, String appName) {
        this.registerService = registerService;
        this.interfaceManager = interfaceManager;
        this.appName = appName;
        nettyService = new NettyService();
        nettyAddress = URLUtil.getLocalIp();
        nettyPort = LOCAL_NETTY_SERVER_PORT;
    }

    /**
     * 启动netty服务端, 监听请求
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 启动zk
        registerService.initRegister(appName);
        //  启动netty服务端
        nettyService.initServer(nettyAddress, nettyPort);
        doRegister();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RpcSeverScan rpcSeverScan = new RpcSeverScan(applicationContext);
        rpcSeverScan.collectResisterList(nettyAddress, nettyPort);
    }

    /**
     * 注册到zk上去
     */
    private void doRegister() {
        for (Register register : RpcServiceCache.getRegisters()) {
            registerService.register(register);
            interfaceManager.setRegisterMap(register.getServiceName(), register);
        }
    }
}
