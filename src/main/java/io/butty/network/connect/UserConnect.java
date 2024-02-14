package io.butty.network.connect;

import io.butty.api.ProxyServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserConnect implements Runnable{
    static final Logger LOGGER = LogManager.getLogger(UserConnect.class);

    ChannelHandlerContext context;
    EventLoopGroup worker;
    ProxyServer server;
    Bootstrap bootstrap;
    Thread thread;

    public UserConnect(ChannelHandlerContext context){
        this.context = context;
    }

    public void setServer(ProxyServer server) {
        this.server = server;
    }

    public void connect(){
        this.worker = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(worker);
        this.bootstrap.channel(NioSocketChannel.class);
        this.bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        this.bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new UserHandle(UserConnect.this));
            }
        });
        this.thread = new Thread(this);
        this.thread.setName(server.getIP()+":"+server.getPort());
    }

    public void close(){
        this.worker.shutdownGracefully();
        this.thread.interrupt();
    }

    @Override
    public void run() {
        try {
            ChannelFuture f = this.bootstrap.connect(server.getIP(), server.getPort()).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            LOGGER.warn("Cannot create a user connect.",e);
        }
    }
}
