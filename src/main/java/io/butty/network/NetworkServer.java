package io.butty.network;

import io.butty.network.raknetty.channel.RakServerChannelOption;
import io.butty.network.raknetty.channel.nio.NioRakServerChannel;
import io.butty.network.raknetty.example.ExampleBedrockPingResponse;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadFactory;

public class NetworkServer {
    private static final Logger LOGGER = LogManager.getLogger(NetworkServer.class);

    ThreadFactory acceptFactory = new DefaultThreadFactory("accept");
    ThreadFactory connectFactory = new DefaultThreadFactory("connect");
    NioEventLoopGroup acceptGroup = new NioEventLoopGroup(1, acceptFactory);
    NioEventLoopGroup connectGroup = new NioEventLoopGroup(connectFactory);
    ServerBootstrap boot;
    int port;

    static {
        ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public NetworkServer(int port) {
        this.boot = new ServerBootstrap();
        this.port = port;
    }

    public void launch() throws InterruptedException {
        try {
            boot = new ServerBootstrap();
            boot.group(acceptGroup, connectGroup)
                    .channel(NioRakServerChannel.class)
                    .option(RakServerChannelOption.RAKNET_GUID, 123456L)
                    .option(RakServerChannelOption.RAKNET_NUMBER_OF_INTERNAL_IDS, 20)
                    .option(RakServerChannelOption.RAKNET_PROTOCOL_VERSION, 10)
                    .option(RakServerChannelOption.RAKNET_MAX_CONNECTIONS, 15)
                    .option(RakServerChannelOption.RAKNET_MAX_MTU_SIZE, 1400)
                    .option(RakServerChannelOption.RAKNET_OFFLINE_RESPONSE, new ExampleBedrockPingResponse())
                    .handler(new LoggingHandler("Reactor", LogLevel.INFO))
                    .childHandler(new LoggingHandler("Connection", LogLevel.INFO));
            ChannelFuture future = boot.bind(port).sync();
            NioRakServerChannel channel = (NioRakServerChannel) future.channel();
            channel.closeFuture().sync();
        }finally {
            acceptGroup.shutdownGracefully();
            connectGroup.shutdownGracefully();
        }
    }
}
