package io.butty;

import io.butty.api.ProxyServer;
import io.butty.network.NetworkServer;
import io.butty.util.Configuration;
import io.butty.util.YamlConfiguration;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class ButtyServer implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(ProxyServer.class);
    List<ProxyServerImp> proxyServers;
    NetworkServer network;
    boolean isRunning;
    String server_name;
    String server_version;
    int port;

    public ButtyServer(Configuration config){
        this.port = config.get("port",19132);
        this.network = new NetworkServer(port);
        this.isRunning = true;
        this.server_name = "ButtyGroup";
        this.server_version = "0.0.1";
    }

    public static void launch(OptionSet option){
        try {
            YamlConfiguration config = new YamlConfiguration();
            LOGGER.info("Loading server config...");
            Configuration configuration = config.load((File) option.valueOf("config"));

            ButtyServer instance = new ButtyServer(configuration);
            LOGGER.info("This server is running in " +instance.server_name + "(API Version "+instance.server_version+")");

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
}
