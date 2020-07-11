package com.roll.rpc.gather.server.netty;

import com.roll.rpc.gather.common.RpcRequest;
import com.roll.rpc.gather.common.RpcResponse;
import com.roll.rpc.gather.common.codec.RpcDecoder;
import com.roll.rpc.gather.common.codec.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * channel处理器
 * created on 2019-09-06 10:22
 */
public class InitialChannel extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast(new RpcDecoder(RpcRequest.class)); // 解码 RPC 请求
        ch.pipeline().addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC 响应
        ch.pipeline().addLast(new RpcServerHandler());
    }
}
