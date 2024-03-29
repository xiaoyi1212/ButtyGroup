package io.butty.network.raknetty.handler.codec.reliability;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.ReliabilityMessage;
import io.butty.util.RakNetUtil;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ConnectionRequestAccepted implements ReliabilityMessage {

    public InetSocketAddress clientAddress;
    public int systemIndex = 0;
    public InetSocketAddress[] ipList;
    public long requestTime;
    public long replyTime;

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_CONNECTION_REQUEST_ACCEPTED;
    }

    @Override
    public void encode(ByteBuf buf) {
        RakNetUtil.writeAddress(buf, clientAddress);
        buf.writeShort(systemIndex);
        for (int i = 0; i < ipList.length; i++) {
            RakNetUtil.writeAddress(buf, ipList[i]);
        }
        buf.writeLong(requestTime);
        buf.writeLong(replyTime);
    }

    @Override
    public void decode(ByteBuf buf) {
        clientAddress = RakNetUtil.readAddress(buf);
        systemIndex = buf.readShort();
        List<InetSocketAddress> list = new ArrayList<>();
        do {
            list.add(RakNetUtil.readAddress(buf));
        } while (buf.readableBytes() > 16);
        ipList = list.toArray(new InetSocketAddress[0]);
        requestTime = buf.readLong();
        replyTime = buf.readLong();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientAddress", clientAddress)
                .append("systemIndex", systemIndex)
                .append("ipList", ipList)
                .append("requestTime", requestTime)
                .append("replyTime", replyTime)
                .toString();
    }
}
