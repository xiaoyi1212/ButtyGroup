package io.butty.api;

import io.butty.ButtyServer;
import io.butty.api.event.Event;
import org.apache.logging.log4j.Logger;

public final class ButtyAPI {
    private ButtyAPI(){}

    static ButtyServer server;

    public static void setInstance(ButtyServer server){
        ButtyAPI.server = server;
    }

    public static void callEvent(Event event){
        server.getPluginManager().callEvent(event);
    }

    public static Logger getLogger(){
        return ButtyServer.getLogger();
    }
}
