package com.roll.rpc.gather.server.rpcserver;

import com.roll.rpc.gather.common.RegisterConstant;
import com.roll.rpc.gather.register.client.Register;
import com.roll.rpc.gather.register.client.RegisterService;
import com.roll.rpc.gather.server.netty.InitialChannel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 * @author roll
 * created on 2019-09-06 09:23
 */
public class RpcServerLauncher implements ApplicationContextAware, InitializingBean {
    /**
     * 具体注册实例
     */
    private RegisterService registerService;

    /**
     * 应用名
     */
    private String appName;

    public RpcServerLauncher(RegisterService registerService, String appName) {
        this.registerService = registerService;
        this.appName = appName;
    }

    /**
     * 启动netty服务端, 监听请求
     */
    public void afterPropertiesSet() {
        EventLoopGroup workferGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(workferGroup, bossGroup);
        bootstrap.channel(NioServerSocketChannel.class);

        bootstrap.childHandler(new InitialChannel());

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

        String[] addressArray = StringUtils.split(RegisterConstant.LOCAL_ADDRESS, ":");
        String ip = addressArray[0];
        int port = Integer.parseInt(addressArray[1]);

        // 启动服务
        try {
            ChannelFuture future = bootstrap.bind(ip, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workferGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    private void doRegister() {
        for (Register register : RpcServiceCache.getRegisters()) {
            registerService.register(register);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RpcServiceCollector rpcServiceCollector = new RpcServiceCollector(appName, applicationContext);
        rpcServiceCollector.collectResisterList();
        doRegister();
    }
}
