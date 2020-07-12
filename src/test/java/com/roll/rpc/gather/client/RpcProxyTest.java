package com.roll.rpc.gather.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>@author roll
 * <p>created on 2020/7/10 4:05 下午
 */
public class RpcProxyTest implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            System.out.println(proxy.getClass().getName());
            // JVM通过这条语句执行原来的方法(反射机制)
            //result = method.invoke(this, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object bind() {
        return Proxy.newProxyInstance(
                HiTest.class.getClassLoader(), new Class[]{HiTest.class}, this);
    }

    public static void main(String[] args) {
        RpcProxyTest rpcProxyTest = new RpcProxyTest();
        HiTest hiTest = (HiTest) rpcProxyTest.bind();
        hiTest.say("12");
    }
}