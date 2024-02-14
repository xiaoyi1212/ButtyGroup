package io.butty.craftbutty;

import io.butty.api.ButtyAPI;
import io.butty.api.ProxyServer;
import io.butty.craftbutty.network.NetworkServer;
import io.butty.craftbutty.util.config.Config;
import io.butty.craftbutty.util.config.SimpleServerConfig;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ProxyServerImp implements ProxyServer,Runnable {

    private static final Logger LOGGER = LogManager.getLogger(ProxyServer.class);
    NetworkServer network;
    SimpleServerConfig config;
    boolean isRunning;
    String server_name;
    String server_version;

    public ProxyServerImp(Config config){
        this.config = config.getConfig();
        this.network = new NetworkServer(this.config.getPort());
        this.isRunning = true;
        this.server_name = "ButtyGroup";
        this.server_version = "0.0.1";
    }

    public static void launch(OptionSet option){
        try {
            Config config = new Config();
            LOGGER.info("Loading server config...");
            config.loadConfig((File) option.valueOf("config"));

            ProxyServerImp instance = new ProxyServerImp(config);
            ButtyAPI.setServer(instance);
            Thread thread = new Thread(instance);
            thread.setName("Server Thread");
            thread.start();
        }catch (Exception e){
            LOGGER.fatal("Server has a exception.",e);
        }
    }

    @Override
    public void run() {
        this.network.launch();
        while (isRunning){

        }
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public String getName() {
        return server_name;
    }

    @Override
    public String getVersion() {
        return server_version;
    }
}
