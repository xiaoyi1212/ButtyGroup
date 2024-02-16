package io.butty.network.raknetty.handler.codec.offline;

import io.netty.buffer.ByteBuf;
import io.butty.network.raknetty.channel.RakServerChannel;

public interface OfflinePingResponse {

    ByteBuf get(RakServerChannel channel);
}
