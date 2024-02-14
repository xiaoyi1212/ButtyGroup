package io.butty.api;

import org.apache.logging.log4j.Logger;

public interface ProxyServer {
    Logger getLogger();
    String getName();
    String getVersion();
}
