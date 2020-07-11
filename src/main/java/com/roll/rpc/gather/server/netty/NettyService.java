package com.roll.rpc.gather.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty 相关操作
 * <p>created on 2020/7/8 10:40 下午
 */
public class NettyService {
    /**
     * 启动Netty 本地服务
     */
    public void initServer(String nettyAddress, String nettyPort) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workerGroup, bossGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new InitialChannel());
        // ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
        // 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // ChannelOption.SO_KEEPALIVE参数对应于套接字选项中的SO_KEEPALIVE，该参数用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的
        // 连接。当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        int port = Integer.parseInt(nettyPort);
        // 启动本地netty服务端
        try {
            ChannelFuture future = bootstrap.bind(nettyAddress, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
