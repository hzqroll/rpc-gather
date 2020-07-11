package com.roll.rpc.gather.client.netty;

import com.roll.rpc.gather.common.RpcRequest;
import com.roll.rpc.gather.common.RpcResponse;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.UUID;

/**
 * 处理服务请求的返回结果
 * <p>created on 2020/7/9 10:15 下午
 */
public class ResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * 发送消息
     *
     * @param request 消息内容
     * @param future  ChannelFuture
     */
    public void writeAndFlush(RpcRequest request, ChannelFuture future) {
        Channel channel = future.channel();
        channel.writeAndFlush(request);
    }
}
