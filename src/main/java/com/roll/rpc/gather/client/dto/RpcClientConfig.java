package com.roll.rpc.gather.client.dto;

/**
 * 客户端配置信息
 * <p>created on 2020/7/10 5:22 下午
 */
public class RpcClientConfig {
    /**
     * 超时时间，单位ms
     */
    private long timeout;

    private int retryTimes;

    private String version;

    public RpcClientConfig(long timeout, int retryTimes, String version) {
        this.timeout = timeout;
        this.retryTimes = retryTimes;
        this.version = version;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
