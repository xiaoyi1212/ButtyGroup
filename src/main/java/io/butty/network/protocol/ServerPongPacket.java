package io.butty.network.protocol;

public class ServerPongPacket extends Packet {
    @Override
    public int getID() {
        return 0;
    }
}
