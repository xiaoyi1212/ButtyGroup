package io.butty.network.protocol;

import io.butty.network.ProtocolManager;

public class ServerHandshakePacket extends Packet {
    @Override
    public int getID() {
        return ProtocolManager.SERVER_HANDSHAKE_PACKET;
    }
}
