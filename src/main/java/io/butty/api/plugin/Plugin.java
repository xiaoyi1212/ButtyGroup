package io.butty.api.plugin;

public interface Plugin {
    PluginDescription getDescription();
    void onEnable();
    void onDisable();

    public enum Type{
        JAVA,JS,PYTHON
    }
}
