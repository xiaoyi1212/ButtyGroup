package io.butty.network.raknetty.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.DatagramChannel;
import io.butty.network.raknetty.handler.codec.offline.OpenConnectionRequest2;
import io.butty.network.raknetty.handler.ipfilter.BannedIpFilter;

import java.net.InetSocketAddress;


public interface RakServerChannel extends ServerChannel {

    void accept(ChannelHandlerContext ctx, OpenConnectionRequest2 request, InetSocketAddress remoteAddress);

    boolean allowNewConnections();

    int numberOfConnections();

    RakChannel getChildChannel(InetSocketAddress address);

    BannedIpFilter banList();

    void removeChildChannel(InetSocketAddress address);

    //RakChannel getChannel(long guid);

    long localGuid();

    long remoteGuid();

    @Override
    RakServerChannelConfig config();

    @Override
    InetSocketAddress localAddress();

    @Override
    InetSocketAddress remoteAddress();

    DatagramChannel udpChannel();
}
