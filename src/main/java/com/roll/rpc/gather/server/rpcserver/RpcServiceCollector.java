package com.roll.rpc.gather.server.rpcserver;

import com.roll.rpc.gather.common.RegisterConstant;
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
public class RpcServiceCollector {
    /**
     * 应用名
     */
    private String appName;

    private ApplicationContext applicationContext;

    public RpcServiceCollector(String appName, ApplicationContext applicationContext) {
        this.appName = appName;
        this.applicationContext = applicationContext;
    }

    public void collectResisterList() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcServer.class);
        for (Object bean : beans.values()) {
            RpcServer rpcServer = bean.getClass().getAnnotation(RpcServer.class);
            String serviceName = rpcServer.value().getName();
            String serviceVersion = rpcServer.version();
            Register register = new Register();
            register.setAppName(appName);
            register.setServiceName(serviceName);
            register.setServiceVersion(serviceVersion);
            register.setAddress(getAddress());
            RpcServiceCache.refreshRegister(register);

            RpcServiceCache.refreshserviceBeanMap(serviceName, bean);
        }
    }

    /**
     * 启动ip+port
     */
    private String getAddress() {
        return RegisterConstant.LOCAL_ADDRESS;
    }
}
