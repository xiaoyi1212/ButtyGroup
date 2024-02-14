package io.butty.craftbutty.network.protocol;

import io.butty.craftbutty.network.ProtocolManager;

public class ClientHandshakePacket extends Packet{
    @Override
    public int getID() {
        return ProtocolManager.CLIENT_HANDSHAKE_PACKET;
    }
}
