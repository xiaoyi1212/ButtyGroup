package io.butty;

import io.butty.network.NetworkServer;
import io.butty.util.ServerConfig;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ButtyServer implements Runnable{

    private static final Logger LOGGER = LogManager.getLogger(ButtyServer.class);
    static ButtyServer instance;

    ServerConfig config;
    NetworkServer network;
    Thread server_thread;
    boolean isRunning;

    public ButtyServer(ServerConfig config){
        this.config = config;
        this.server_thread = new Thread(this);
        this.isRunning = true;
        this.server_thread.setName("Server Thread");
        this.server_thread.start();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void start(OptionSet option){
        LOGGER.info("Launching server...");
        ServerConfig serverConfig = new ServerConfig(option);
        ButtyServer.instance = new ButtyServer(serverConfig);
    }

    @Override
    public void run() {
        try {
            this.network = new NetworkServer(config.getPort());
            this.network.launch();
            while (isRunning){

            }
        }catch (Exception e){
            LOGGER.fatal("Server thread has a exception.",e);
        }
    }
}
