package io.butty.network.raknetty.handler.codec.reliability;

import io.butty.network.raknetty.handler.codec.InternalPacket;

public class HeapedPacket implements Comparable<HeapedPacket> {

    int weight;
    InternalPacket packet;

    public HeapedPacket(int weight, InternalPacket packet) {
        this.weight = weight;
        this.packet = packet;
    }

    @Override
    public int compareTo(HeapedPacket o) {
        return Integer.compare(weight, o.weight);
    }
}
