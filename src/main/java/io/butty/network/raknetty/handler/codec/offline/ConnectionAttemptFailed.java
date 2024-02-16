package io.butty.network.raknetty.handler.codec.offline;

import io.butty.network.raknetty.handler.codec.MessageIdentifier;
import io.butty.network.raknetty.handler.codec.OfflineMessageAdapter;

public class ConnectionAttemptFailed extends OfflineMessageAdapter {

    @Override
    public MessageIdentifier getId() {
        return MessageIdentifier.ID_CONNECTION_ATTEMPT_FAILED;
    }
}
