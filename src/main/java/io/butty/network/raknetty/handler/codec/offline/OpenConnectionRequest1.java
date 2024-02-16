package io.butty.network.raknetty.handler.codec.offline;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.butty.network.raknetty.handler.codec.Message;
import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.OfflineMessage;
import io.butty.util.RakNetUtil;

public class OpenConnectionRequest1 implements OfflineMessage {

    public int protocol;
    public int mtuSize;

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_OPEN_CONNECTION_REQUEST_1;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID);
        buf.writeByte(protocol);
        RakNetUtil.padWithZero(buf, mtuSize - Message.UDP_HEADER_SIZE);
    }

    @Override
    public void decode(ByteBuf buf) {
        mtuSize = buf.readableBytes() + 1 + Message.UDP_HEADER_SIZE;
        buf.skipBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID.length);
        protocol = buf.readByte();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("protocol", protocol)
                .append("mtuSize", mtuSize)
                .toString();
    }
}
