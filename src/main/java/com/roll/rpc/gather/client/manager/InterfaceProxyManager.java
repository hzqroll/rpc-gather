package com.roll.rpc.gather.client.manager;

import com.roll.rpc.gather.client.dto.RpcClientConfig;
import com.roll.rpc.gather.common.InterfaceManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Proxy;

/**
 * <p>created on 2020/7/11 11:51 上午
 */
@Service
public class InterfaceProxyManager implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    private DefaultListableBeanFactory beanFactory;

    private final InterfaceManager interfaceManager;

    public InterfaceProxyManager(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        beanFactory = (DefaultListableBeanFactory) this.applicationContext.getBeanFactory();
    }

    /**
     * 客户端注册远程接口
     *
     * @param interfaceClass  接口
     * @param rpcClientConfig 配置信息
     */
    public void registerProxy(Class<?> interfaceClass, RpcClientConfig rpcClientConfig) {
        Object interfaceProxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new RpcClientProxy(interfaceManager));
        String beanName = interfaceClass.getSimpleName();
        String classPath = interfaceClass.getName();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(interfaceClass.cast(interfaceProxyInstance).getClass());
        beanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
        interfaceManager.setRpcClientConfigMap(classPath, rpcClientConfig);
        interfaceManager.setProxyRegisterMap(interfaceProxyInstance.getClass().getName(), classPath);
    }
}
