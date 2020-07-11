package com.roll.rpc.gather.client.manager;

import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Netty客户端，用来与服务端通信，发送调用信息
 * <p>
 * created on 2019-09-06 15:50
 */
@Service
public class RpcClientProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        Object result = null;

        return null;
    }
}
