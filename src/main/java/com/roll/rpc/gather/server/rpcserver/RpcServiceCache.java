package com.roll.rpc.gather.server.rpcserver;


import com.roll.rpc.gather.register.client.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待注册服务缓存
 * 被注册的服务bean缓存
 *
 * @author roll
 * created on 2019-09-06 14:19
 */
public final class RpcServiceCache {
    /**
     * 待注册服务缓存
     */
    private static final List<Register> registers;

    /**
     * 被注册的服务bean缓存, 验证服务的ref是否有效
     */
    private static final Map<String, Object> serviceBeanMap;

    static {
        registers = new ArrayList<>();
        serviceBeanMap = new HashMap<>();
    }

    public static void refreshServiceBeanMap(String beanName, Object bean) {
        serviceBeanMap.put(beanName, bean);
    }

    /**
     * 不支持多包同样路径的类
     */
    public static Object getServiceBean(String beanName) {
        return serviceBeanMap.get(beanName);
    }

    public static void refreshRegister(Register register) {
        registers.add(register);
    }

    public static List<Register> getRegisters() {
        return registers;
    }
}
