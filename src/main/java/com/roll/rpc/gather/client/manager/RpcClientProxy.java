package com.roll.rpc.gather.client.manager;

import com.roll.rpc.gather.client.dto.RpcClientConfig;
import com.roll.rpc.gather.common.InterfaceManager;
import com.roll.rpc.gather.common.RpcRequest;
import com.roll.rpc.gather.common.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Netty客户端，用来与服务端通信，发送调用信息
 * <p>
 * created on 2019-09-06 15:50
 */
public class RpcClientProxy extends SimpleChannelInboundHandler<RpcResponse> implements InvocationHandler {

    private final InterfaceManager interfaceManager;

    public RpcClientProxy(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    private Object response;

    private Throwable cause;

    /**
     * 等待调用结果，并且返回
     * todo 超时设置
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String proxyName = proxy.getClass().getName();
        String interfaceName = interfaceManager.getInterfaceName(proxyName);
        RpcClientConfig rpcClientConfig = interfaceManager.getRpcClientConfigMap(interfaceName);
        ChannelFuture channelFuture = interfaceManager.getNettyClientChannelFuture(interfaceName);
        Channel channel = channelFuture.channel();
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(interfaceName);
        rpcRequest.setServiceVersion(rpcClientConfig.getVersion());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);
        rpcRequest.setRequestId("1");
        channel.writeAndFlush(rpcRequest);
        channelFuture.syncUninterruptibly();
        if (cause != null) {
            throw cause;
        }
        return response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) {
        response = msg.getResult();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        this.cause = cause;
    }
}
