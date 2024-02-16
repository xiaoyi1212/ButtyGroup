package io.butty;

import io.butty.api.plugin.PluginManager;
import io.butty.network.NetworkServer;
import io.butty.util.ServerConfig;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ButtyServer implements Runnable{

    private static final Logger LOGGER = LogManager.getLogger(ButtyServer.class);
    static ButtyServer instance;

    ServerConfig config;
    NetworkServer network;
    Thread server_thread;
    PluginManager pluginManager;
    boolean isRunning;

    public ButtyServer(ServerConfig config){
        this.config = config;
        this.server_thread = new Thread(this);
        this.isRunning = true;
        this.server_thread.setName("Server Thread");
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void start(OptionSet option){
        LOGGER.info("Launching server...");
        ServerConfig serverConfig = new ServerConfig(option);
        ButtyServer.instance = new ButtyServer(serverConfig);
        PluginManager manager = new PluginManager((File) option.valueOf("plugins"));
        ButtyServer.instance.pluginManager = manager;
        ButtyServer.instance.launch();
    }

    public void launch(){
        this.server_thread.start();
        this.pluginManager.loadJavaPlugins();
        LOGGER.info("Server load Done! Type '/help'.");
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public void run() {
        try {
            this.network = new NetworkServer(config.getPort());
            this.network.launch();
        }catch (Exception e){
            LOGGER.error("Server was throw exception.", e);
        }
    }
}
