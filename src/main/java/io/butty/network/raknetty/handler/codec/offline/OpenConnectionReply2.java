package io.butty.network.raknetty.handler.codec.offline;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.OfflineMessage;
import io.butty.util.RakNetUtil;

import java.net.InetSocketAddress;

public class OpenConnectionReply2 implements OfflineMessage {

    public long serverGuid;
    public InetSocketAddress clientAddress;
    public int mtuSize;
    public final boolean hasSecurity = false;   // TODO: implement security

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_OPEN_CONNECTION_REPLY_2;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID);
        buf.writeLong(serverGuid);
        RakNetUtil.writeAddress(buf, clientAddress);
        buf.writeShort(mtuSize);
        buf.writeBoolean(hasSecurity);
    }

    @Override
    public void decode(ByteBuf buf) {
        buf.skipBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID.length);
        serverGuid = buf.readLong();
        clientAddress = RakNetUtil.readAddress(buf);
        mtuSize = buf.readShort();
        buf.skipBytes(1);                      // TODO: implement security
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("serverGuid", serverGuid)
                .append("clientAddress", clientAddress)
                .append("mtuSize", mtuSize)
                .append("hasSecurity", hasSecurity)
                .toString();
    }
}
