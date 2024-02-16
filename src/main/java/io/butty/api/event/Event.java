package io.butty.api.event;

public abstract class Event {
    public String getEventName(){
        return this.getClass().getSimpleName();
    }
}
