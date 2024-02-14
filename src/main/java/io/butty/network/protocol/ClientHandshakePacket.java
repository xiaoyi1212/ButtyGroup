package io.butty.network.protocol;

import io.butty.network.ProtocolManager;

public class ClientHandshakePacket extends Packet{
    @Override
    public int getID() {
        return ProtocolManager.CLIENT_HANDSHAKE_PACKET;
    }
}
