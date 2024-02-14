package io.butty.craftbutty.network.protocol;

public class ClientPingPacket extends Packet {
    @Override
    public int getID() {
        return 0;
    }
}
