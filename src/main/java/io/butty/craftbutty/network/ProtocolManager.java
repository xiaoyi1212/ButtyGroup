package io.butty.craftbutty.network;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import io.butty.craftbutty.network.protocol.Packet;
import io.butty.craftbutty.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class ProtocolManager {
    int CURRENT_PROTOCOL = Util.dynamic(649);

    String MINECRAFT_VERSION_NETWORK = Util.dynamic("1.20.60");

    public static int LOGIN_PACKET = 0x01;
    public static int PLAY_STATUS_PACKET = 0x02;
    public static int SERVER_HANDSHAKE_PACKET = 0x03;
    public static int CLIENT_HANDSHAKE_PACKET = 0x04;
    public static int DISCONNECT_PACKET = 0x05;

    static Int2ObjectOpenHashMap<ConstructorAccess<? extends Packet>> packets = new Int2ObjectOpenHashMap<>(256);

    public static void registerPacket(Integer id,Class<? extends Packet> packet){
        packets.putIfAbsent(id,ConstructorAccess.get(packet));
    }

    public static Packet get(Integer id){
        ConstructorAccess<? extends Packet> packet = packets.get(id);
        if(packet == null) return null;
        return packet.newInstance();
    }
}
