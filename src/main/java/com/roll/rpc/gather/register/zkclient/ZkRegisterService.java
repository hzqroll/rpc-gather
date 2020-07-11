package com.roll.rpc.gather.register.zkclient;

import com.roll.rpc.gather.common.RegisterConstant;
import com.roll.rpc.gather.component.zk.ZkClient;
import com.roll.rpc.gather.register.client.Register;
import com.roll.rpc.gather.register.client.RegisterService;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * zk注册中心实现
 *
 * @author roll
 * created on 2019-09-05 15:27
 */
@Service
public class ZkRegisterService implements RegisterService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("zkLog");

    private String appName;
    /**
     * zkClient客户端
     */
    private final ZkClient zkClient;

    public ZkRegisterService(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void initRegister(String appName) throws Exception {
        this.appName = appName;
        if (zkClient.exist(getAppPath(appName))) {
            logger.info("zkClient init appPath, appName: {}", appName);
            zkClient.create(getAppPath(appName), appName.getBytes(), CreateMode.PERSISTENT);
        }
    }

    /**
     * 注册节点
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 是否注册成功
     */
    @Override
    public boolean register(Register register) {
        try {
            // checkExist 检查是否存在，不存在返回null
            zkClient.create(getServicePath(register), (register.getAddress() + ":" + register.getPort()).getBytes(), CreateMode.PERSISTENT);
            logger.info("zkClient register success, register: {}", register);
        } catch (Exception e) {
            logger.error("zkClient register failed, register: {}.", register, e);
            return false;
        }
        return true;
    }

    /**
     * 删除节点
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 是否删除成功
     */
    @Override
    public boolean unRegister(Register register) {
        try {
            zkClient.delete(getServicePath(register));
        } catch (Exception e) {
            logger.error("zkClient unRegister failed, register: {}.", register, e);
            return false;
        }
        return true;
    }

    /**
     * 获取节点数据
     *
     * @param register 服务名:应用名/类名-版本, {@link Register}
     * @return 节点数据（服务地址）
     */
    @Override
    public List<String> lookup(Register register) {
        try {
            if (!zkClient.exist(getServicePath(register))) {
                return null;
            }
            List<String> address = zkClient.getChild(getServiceVersionPath(register));
            logger.info("zkClient get address: {}", address);
            return address;
        } catch (Exception e) {
            logger.error("zkClient lookup failed, register: {}.", register, e);
        }
        return new ArrayList<>(0);
    }

    @Override
    public void registerServerAddress(String appName, String url) {
        try {
            if (!zkClient.exist(getServerPath(url))) {
                // checkExist 检查是否存在，不存在返回null
                zkClient.create(getServerPath(url), url.getBytes(), CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            logger.error("zkClient register failed, url: {}.", url, e);
        }
    }

    @Override
    public List<String> getServerAddress(String appName) {
        try {
            return zkClient.getChild(getServerPath());
        } catch (Exception e) {

        }
        return new ArrayList<>();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        zkClient.start();
        if (!zkClient.exist(RegisterConstant.SERVICE_ROOT)) {
            try {
                zkClient.create(RegisterConstant.SERVICE_ROOT, "rootNode".getBytes(), CreateMode.PERSISTENT);
            } catch (Exception e) {
                logger.info("zkClient init error. ", e);
                throw e;
            }
        }
    }


    /**
     * 获取注册全路径
     */
    private String getServicePath(Register register) {
        return getAppPath(appName) + "/" + register.getServiceName() + register.getServiceVersion() + "/" + register.getAddress() + ":" + register.getPort();
    }

    /**
     * 获取注册全路径
     */
    private String getServiceVersionPath(Register register) {
        return getAppPath(appName) + "/" + register.getServiceName() + register.getServiceVersion();
    }

    /**
     * 获取应用注册路径
     */
    private String getAppPath(String appName) {
        return RegisterConstant.SERVICE_ROOT + "/" + appName;
    }

    /**
     * 获取注册的服务端地址
     *
     * @return 地址
     */
    private String getServerPath(String url) {
        return getAppPath(appName) + "/" + "server/" + url;
    }

    /**
     * 获取注册的服务端地址
     *
     * @return 地址
     */
    private String getServerPath() {
        return getAppPath(appName) + "/" + "server";
    }
}
