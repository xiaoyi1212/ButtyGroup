package io.butty;

import io.butty.api.ProxyServer;
import io.netty.buffer.ByteBuf;

public class ProxyServerImp implements ProxyServer {

    @Override
    public int getPort() {
        return 0;
    }

    @Override
    public String getIP() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void sendData(ByteBuf byteBuf) {

    }

}
