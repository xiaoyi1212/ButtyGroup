package io.butty.network.raknetty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.ByteBufFormat;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import io.butty.network.raknetty.channel.RakChannel;
import io.butty.network.raknetty.channel.RakChannelOption;
import io.butty.network.raknetty.channel.RakServerChannel;
import io.butty.network.raknetty.channel.RakServerChannelOption;
import io.butty.network.raknetty.channel.nio.NioRakChannel;
import io.butty.network.raknetty.channel.nio.NioRakServerChannel;
import io.butty.network.raknetty.handler.codec.PacketPriority;
import io.butty.network.raknetty.handler.codec.PacketReliability;

import java.util.concurrent.ThreadFactory;

public class BedrockForwarder {

    static final int PORT = 19132;
    static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(BedrockForwarder.class);

    static RakChannel clientChannel;
    static RakChannel serverChildChannel;
    static RakServerChannel serverChannel;

    static {
        ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static void main(String[] args) throws Exception {
        startClient();
        startServer();
    }

    public static void startServer() throws Exception {
        final ThreadFactory acceptFactory = new DefaultThreadFactory("server-accept");
        final ThreadFactory connectFactory = new DefaultThreadFactory("server-connect");
        final NioEventLoopGroup acceptGroup = new NioEventLoopGroup(2, acceptFactory);
        final NioEventLoopGroup connectGroup = new NioEventLoopGroup(connectFactory);

        final ServerBootstrap boot = new ServerBootstrap();
        boot.group(acceptGroup, connectGroup)
                .channel(NioRakServerChannel.class)
                .option(RakServerChannelOption.RAKNET_GUID, 123456L)
                // consist with the bedrock server's RakNet configuration
                .option(RakServerChannelOption.RAKNET_MAX_CONNECTIONS, 1)
                .option(RakServerChannelOption.RAKNET_MAX_MTU_SIZE, 1400)
                .option(RakServerChannelOption.RAKNET_OFFLINE_RESPONSE, new ExampleBedrockPingResponse())
                .handler(new LoggingHandler("RakServerLogger", LogLevel.DEBUG, ByteBufFormat.SIMPLE))
                .childOption(RakChannelOption.RAKNET_NUMBER_OF_INTERNAL_IDS, 20)
                .childHandler(new ChannelInitializer<RakChannel>() {
                    @Override
                    public void initChannel(final RakChannel ch) throws Exception {
                        serverChildChannel = ch;
                        ch.pipeline().addLast(new ChannelDuplexHandler() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // use RakNetty client to send, use assumed priority and reliability
                                ByteBuf buf = (ByteBuf) msg;
                                clientChannel.send(buf, PacketPriority.HIGH_PRIORITY, PacketReliability.RELIABLE_ORDERED, 0);
                            }

                            @Override
                            public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                                clientChannel.disconnect();
                                super.close(ctx, promise);
                            }
                        });
                        ch.pipeline().addLast(new LoggingHandler("ChannelLogger", LogLevel.DEBUG, ByteBufFormat.SIMPLE) {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                ctx.write(msg, promise);
                                // disable log here
                            }
                        });
                        // close the main channel if the child channel is on close
                        ch.closeFuture().addListener((ChannelFutureListener) future -> serverChannel.close());
                    }
                });
        // Start the server.
        final ChannelFuture future = boot.bind(PORT).sync();
        LOGGER.info("RakNetty server is ready.");

        serverChannel = (NioRakServerChannel) future.channel();
        serverChannel.closeFuture().addListener((ChannelFutureListener) future1 -> {
            LOGGER.info("RakNetty server is closed.");
            // close the workgroup when shutting down the server
            acceptGroup.shutdownGracefully();
            connectGroup.shutdownGracefully();
            // close the client if it is open
            clientChannel.close();
        });
    }

    public static void startClient() throws Exception {
        final ThreadFactory factory = new DefaultThreadFactory("client");
        final NioEventLoopGroup workGroup = new NioEventLoopGroup(factory);

        final Bootstrap boot = new Bootstrap();
        boot.group(workGroup)
                .channel(NioRakChannel.class)
                .option(RakChannelOption.RAKNET_GUID, 654321L)
                // consist with the bedrock RakNet configuration
                .option(RakChannelOption.RAKNET_CONNECT_INTERVAL, 500)
                .option(RakChannelOption.RAKNET_CONNECT_ATTEMPTS, 12)
                .option(RakChannelOption.RAKNET_NUMBER_OF_INTERNAL_IDS, 20)
                .handler(new ChannelInitializer<NioRakChannel>() {
                    @Override
                    protected void initChannel(NioRakChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelDuplexHandler() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // use RakNetty client to send, use assumed priority and reliability
                                ByteBuf buf = (ByteBuf) msg;
                                serverChildChannel.send(buf, PacketPriority.HIGH_PRIORITY, PacketReliability.RELIABLE_ORDERED, 0);
                            }

                            @Override
                            public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                                serverChildChannel.disconnect();
                                super.close(ctx, promise);
                            }
                        });
                        ch.pipeline().addLast(new LoggingHandler("RakLogger", LogLevel.DEBUG, ByteBufFormat.SIMPLE));
                    }
                });
        // Start the server.
        final ChannelFuture future = boot.connect("pe.mineplex.com", 19132).sync();
        LOGGER.info("RakNetty client is connected successfully.");

        clientChannel = (RakChannel) future.channel();
        clientChannel.closeFuture().addListener((ChannelFutureListener) future1 -> {
            LOGGER.info("RakNetty client is closed.");
            // close the workgroup
            workGroup.shutdownGracefully();
            // close the server if it is open
            serverChannel.close();
        });
    }
}
