package com.roll.rpc.gather.server.rpcserver;

import com.roll.rpc.gather.register.client.Register;
import com.roll.rpc.gather.server.RpcServer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 启动之后,获取注解了{@link RpcServer}的注解, 注册为服务
 *
 * @author roll
 * created on 2019-09-05 20:28
 */
@Component
public class RpcSeverScan {

    private final ApplicationContext applicationContext;

    public RpcSeverScan(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取注解的服务提供者
     *
     * @param address 注册到的服务端ip：port
     */
    public void collectResisterList(String address, String port) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcServer.class);
        for (Object bean : beans.values()) {
            RpcServer rpcServer = bean.getClass().getAnnotation(RpcServer.class);
            String serviceName = rpcServer.value().getName();
            String serviceVersion = rpcServer.version();
            Register register = new Register();
            register.setServiceName(serviceName);
            register.setServiceVersion(serviceVersion);
            register.setAddress(address);
            register.setPort(port);
            register.setTimeout(rpcServer.timeout());
            register.setRetryTimes(rpcServer.retryTimes());
            // 已经注册的register
            RpcServiceCache.refreshRegister(register);
            // 类名和register对应
            RpcServiceCache.refreshServiceBeanMap(serviceName, bean);
        }
    }
}
