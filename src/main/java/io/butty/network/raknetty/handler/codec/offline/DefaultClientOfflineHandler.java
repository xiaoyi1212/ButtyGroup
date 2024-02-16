package io.butty.network.raknetty.handler.codec.offline;

import io.butty.network.raknetty.handler.codec.reliability.ConnectionRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.butty.network.raknetty.channel.AddressedOfflineMessage;
import io.butty.network.raknetty.channel.RakChannel;
import io.butty.network.raknetty.handler.codec.OfflineMessage;
import io.butty.network.raknetty.handler.codec.PacketPriority;
import io.butty.network.raknetty.handler.codec.PacketReliability;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class DefaultClientOfflineHandler extends AbstractOfflineHandler {
    private final static InternalLogger LOGGER = InternalLoggerFactory.getInstance(DefaultClientOfflineHandler.class);
    public static final String NAME = "ClientOffline";

    //private final SocketAddress remoteAddress;

    public DefaultClientOfflineHandler(RakChannel channel) {
        super(channel);
    }

    @Override
    public RakChannel channel() {
        return (RakChannel) super.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof DatagramPacket) {
            DatagramPacket packet = (DatagramPacket) msg;
            InetSocketAddress sender = packet.sender();

            if (!sender.equals(channel().remoteAddress())) {
                LOGGER.debug("datagram from unknown sender: {}, expecting: {}", sender, channel().remoteAddress());
                return;
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    public void readOfflinePacket(ChannelHandlerContext ctx, OfflineMessage msg, InetSocketAddress sender) {

        if (channel().isActive() || channel().connectMode() == RakChannel.ConnectMode.REQUESTED_CONNECTION) {
            LOGGER.debug("offline message from a connected system: {}, discarding", sender);
            return;
        }

        long now = System.nanoTime();
        //LOGGER.debug("READ: {}", msg);

        if (msg instanceof OpenConnectionReply1) {
            OpenConnectionReply1 in = (OpenConnectionReply1) msg;

            OpenConnectionRequest2 out = new OpenConnectionRequest2();
            out.serverAddress = sender;
            out.mtuSize = in.mtuSize;
            out.clientGuid = channel().localGuid();

            ctx.writeAndFlush(new AddressedOfflineMessage(out, sender));

        } else if (msg instanceof OpenConnectionReply2) {
            OpenConnectionReply2 in = (OpenConnectionReply2) msg;
            channel().connectMode(RakChannel.ConnectMode.REQUESTED_CONNECTION);

            // update the channel's mtu size
            channel().mtuSize(in.mtuSize);

            ConnectionRequest out = new ConnectionRequest();
            out.clientGuid = channel().localGuid();
            out.requestTime = TimeUnit.NANOSECONDS.toMillis(now);

            channel().send(out, PacketPriority.IMMEDIATE_PRIORITY, PacketReliability.RELIABLE, 0);
        }
    }
}
