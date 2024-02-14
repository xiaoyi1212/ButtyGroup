package io.butty.api;

import io.netty.buffer.ByteBuf;

public interface ProxyServer {
    int getPort();
    String getIP();
    String getName();
    void sendData(ByteBuf byteBuf);
}
