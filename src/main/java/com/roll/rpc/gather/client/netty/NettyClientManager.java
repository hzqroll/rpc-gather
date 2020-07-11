package com.roll.rpc.gather.client.netty;

import com.roll.rpc.gather.common.RpcRequest;
import com.roll.rpc.gather.common.RpcResponse;
import com.roll.rpc.gather.common.codec.RpcDecoder;
import com.roll.rpc.gather.common.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Netty 客户端管理
 * created on 2019-09-06 15:45
 */
public class NettyClientManager {

    public Bootstrap getBootstrap() {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventExecutors);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                pipeline.addLast(new RpcEncoder(RpcRequest.class));
                pipeline.addLast(new RpcDecoder(RpcResponse.class));
                pipeline.addLast(new ResponseMessageHandler());
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }
}
