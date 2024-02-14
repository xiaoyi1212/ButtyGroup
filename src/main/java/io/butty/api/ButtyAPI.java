package io.butty.api;

public final class ButtyAPI {
    private ButtyAPI(){}

    static ProxyServer server;

    public static void setServer(ProxyServer server){
        ButtyAPI.server = server;
    }

    public static ProxyServer getServer() {
        return server;
    }

}
