package io.butty.network.raknetty.channel;

import io.butty.network.raknetty.handler.codec.offline.OfflinePingResponse;

public interface RakServerChannelConfig extends SharedChannelConfig {

    int getMaximumConnections();

    RakServerChannelConfig setMaximumConnections(int maxConnections);

    int getMaximumMtuSize();

    RakServerChannelConfig setMaximumMtuSize(int maxMtuSize);

    OfflinePingResponse getOfflinePingResponse();

    RakServerChannelConfig setOfflinePingResponseBuilder(OfflinePingResponse response);
}
