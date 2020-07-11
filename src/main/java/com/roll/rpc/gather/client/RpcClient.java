package com.roll.rpc.gather.client;

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
 * RPC 客户端，与服务端收发消息
 * 继承了SimpleChannelInboundHandler类
 * created on 2019-09-03 20:07
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
    private final String host;

    private final int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) {
        this.response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        response.setException((Exception) cause);
    }

    RpcResponse send(RpcRequest request) throws Exception {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            // 创建并初始化netty客户端 BootStrap对象
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                    pipeline.addLast(new RpcEncoder(RpcRequest.class)); // 编码 RPC 请求
                    pipeline.addLast(new RpcDecoder(RpcResponse.class)); // 解码 RPC 响应
                    pipeline.addLast(RpcClient.this); // 处理 RPC 响应
                }
            });
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // connect rpc server
            ChannelFuture future = bootstrap.connect(host, port).sync();
            Channel channel = future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            return response;
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
