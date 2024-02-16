package io.butty.network.raknetty.handler.codec.reliability;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.ReliabilityMessage;
import io.butty.util.RakNetUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class NewIncomingConnection implements ReliabilityMessage {

    public InetSocketAddress serverAddress;
    public InetSocketAddress[] clientAddresses;
    public long pingTime;
    public long pongTime;

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_NEW_INCOMING_CONNECTION;
    }

    @Override
    public void encode(ByteBuf buf) {
        RakNetUtil.writeAddress(buf, serverAddress);
        for (int i = 0; i < clientAddresses.length; i++) {
            RakNetUtil.writeAddress(buf, clientAddresses[i]);
        }
        buf.writeLong(pingTime);
        buf.writeLong(pongTime);
    }

    @Override
    public void decode(ByteBuf buf) {
        serverAddress = RakNetUtil.readAddress(buf);
        List<InetSocketAddress> list = new ArrayList<>();
        do {
            list.add(RakNetUtil.readAddress(buf));
        } while (buf.readableBytes() > 16);
        clientAddresses = list.toArray(new InetSocketAddress[0]);
        pingTime = buf.readLong();
        pongTime = buf.readLong();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("serverAddress", serverAddress)
                .append("clientAddresses", clientAddresses)
                .append("pingTime", pingTime)
                .append("pongTime", pongTime)
                .toString();
    }
}
