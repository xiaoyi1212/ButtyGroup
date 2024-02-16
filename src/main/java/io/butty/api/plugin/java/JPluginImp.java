package io.butty.api.plugin.java;

import io.butty.api.plugin.Plugin;
import io.butty.api.plugin.PluginDescription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URLClassLoader;

public class JPluginImp implements Plugin {
    PluginDescription description;
    JavaPlugin instance;
    URLClassLoader loader;
    Logger logger;
    boolean isEnable;

    public JPluginImp(PluginDescription description, JavaPlugin instance, URLClassLoader loader) {
        this.description = description;
        this.instance = instance;
        this.loader = loader;
        this.logger = LogManager.getLogger(description.getName());
    }

    @Override
    public PluginDescription getDescription() {
        return description;
    }

    @Override
    public void onEnable() {
        logger.info("Enable '{}'.",description.getName());
        this.instance.onEnable();
        this.isEnable = true;
    }

    @Override
    public void onDisable() {
        try {
            logger.info("Disable '{}'.", description.getName());
            this.instance.onDisable();
            this.isEnable = false;
            this.loader.close();
        }catch (IOException e){
            logger.info("Disable '{}' was throw exception..", description.getName(),e);
        }
    }
}
