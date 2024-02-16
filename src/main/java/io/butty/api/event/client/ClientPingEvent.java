package io.butty.api.event.client;

import io.butty.api.event.Event;

public class ClientPingEvent extends Event {
    String motd1,motd2;

    public ClientPingEvent(String[] motds){
        this.motd1 = motds[0];
        this.motd2 = motds[1];
    }

    public void setMotd1(String motd1) {
        this.motd1 = motd1;
    }

    public void setMotd2(String motd2) {
        this.motd2 = motd2;
    }

    public String getMotd1() {
        return motd1;
    }

    public String getMotd2() {
        return motd2;
    }
}
