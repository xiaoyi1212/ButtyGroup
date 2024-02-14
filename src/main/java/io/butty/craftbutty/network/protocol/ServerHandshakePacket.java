package io.butty.craftbutty.network.protocol;

import io.butty.craftbutty.network.ProtocolManager;

public class ServerHandshakePacket extends Packet {
    @Override
    public int getID() {
        return ProtocolManager.SERVER_HANDSHAKE_PACKET;
    }
}
