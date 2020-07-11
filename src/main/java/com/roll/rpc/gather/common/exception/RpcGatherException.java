package com.roll.rpc.gather.common.exception;

/**
 * RPC 异常类型
 * <p>created on 2020/7/7 5:22 下午
 */
public class RpcGatherException extends RuntimeException {
    public RpcGatherException() {
    }

    public RpcGatherException(String message) {
        super(message);
    }
}
