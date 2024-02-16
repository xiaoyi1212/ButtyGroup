package io.butty.network.raknetty.channel;

import io.netty.channel.DefaultAddressedEnvelope;
import io.butty.network.raknetty.handler.codec.OfflineMessage;

import java.net.InetSocketAddress;

public class AddressedOfflineMessage extends DefaultAddressedEnvelope<OfflineMessage, InetSocketAddress> {
    public AddressedOfflineMessage(OfflineMessage message, InetSocketAddress recipient, InetSocketAddress sender) {
        super(message, recipient, sender);
    }

    public AddressedOfflineMessage(OfflineMessage message, InetSocketAddress recipient) {
        super(message, recipient);
    }
}
