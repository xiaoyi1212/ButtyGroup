package io.butty.api;

import org.apache.logging.log4j.Logger;

public final class ButtyAPI {
    private ButtyAPI(){}

    static ProxyServer server;

    public static void setServer(ProxyServer server){
        ButtyAPI.server = server;
        ButtyAPI.server.getLogger().info("This server is running in " +server.getName() + "(API Version "+server.getVersion()+")");
    }

    public static ProxyServer getServer() {
        return server;
    }

    public static Logger getLogger(){
        return server.getLogger();
    }
}
