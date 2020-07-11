package com.roll.rpc.gather.register.client;

/**
 * 服务注册对象
 *
 * @author roll
 * created on 2019-09-05 16:30
 */
public class Register {
    /**
     * 类名
     */
    private String serviceName;

    /**
     * 版本号,默认1.0.0，节点尾部
     */
    private String serviceVersion = "1.0.0";

    /**
     * 地址，客户端netty链接的服务端地址和接口
     */
    private String address;

    private String port;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 重试次数
     */
    private int retryTimes;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Register{" +
                "serviceName='" + serviceName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", address='" + address + '\'' +
                ", timeout=" + timeout +
                ", retryTimes=" + retryTimes +
                '}';
    }
}
