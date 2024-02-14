package io.butty.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkServer implements Runnable{

    private static final Logger LOGGER = LogManager.getLogger(NetworkServer.class);

    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    ServerBootstrap bootstrap;
    ServerChannel server;
    Thread thread;
    int port;

    public NetworkServer(int port){
        try {
            LOGGER.info("Launching network service...");
            this.bossGroup = new NioEventLoopGroup();
            this.workerGroup = new NioEventLoopGroup();
            this.bootstrap = new ServerBootstrap();
            this.bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new PacketHandler());
                        }
                    });
            this.port = port;
        }catch (Exception e){
            LOGGER.fatal("Server launch failed.",e);
            shutdown();
        }
    }

    public void launch(){
        thread = new Thread(this);
        thread.setName("Network IO");
        thread.setDaemon(true);
        thread.start();
    }

    public void shutdown(){
        LOGGER.info("Closing Network IO...");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        server.close();
        thread.interrupt();
    }

    @Override
    public void run() {
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            server = (ServerChannel) future.channel();
            LOGGER.info("Sever port: " + port);
            server.closeFuture().sync();
        }catch (Exception e){
            LOGGER.fatal("Server launch failed.",e);
        }
    }
}
