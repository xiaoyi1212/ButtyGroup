package io.butty.network.raknetty.handler.codec.offline;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.OfflineMessage;

public class IncompatibleProtocolVersion implements OfflineMessage {

    public int protocol;
    public long senderGuid;

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_INCOMPATIBLE_PROTOCOL_VERSION;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeByte(protocol);
        buf.writeBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID);
        buf.writeLong(senderGuid);
    }

    @Override
    public void decode(ByteBuf buf) {
        protocol = buf.readByte();
        buf.skipBytes(OfflineMessage.OFFLINE_MESSAGE_DATA_ID.length);
        senderGuid = buf.readLong();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("protocol", protocol)
                .append("senderGuid", senderGuid)
                .toString();
    }
}
