package io.butty.util;

import io.butty.ButtyServer;
import joptsimple.OptionSet;

import java.io.*;

public class ServerConfig {
    YamlConfiguration configuration;
    int port;
    String motd[];

    public ServerConfig(OptionSet option){
        this.configuration = new YamlConfiguration();
        this.motd = new String[]{"",""};
        loadConfig((File) option.valueOf("config"));
    }

    public void createConfigFile(File file) throws IOException {
        if(!file.exists()) {
            file.createNewFile();
            try(BufferedOutputStream target = new BufferedOutputStream(new FileOutputStream(file))){
                InputStream source = Util.getResource("config.yml");
                byte[] buf = new byte[8192];
                int length;
                while ((length = source.read(buf)) > 0) {
                    target.write(buf, 0, length);
                }
            }
        }
    }

    public void loadConfig(File file){
        try {
            createConfigFile(file);
            Configuration config = this.configuration.load(file);

            this.port = config.get("port",19132);
            this.motd[0] = config.get("motd1","ButtyGroup");
            this.motd[1] = config.get("motd2","Welcome");

        } catch (IOException e) {
            ButtyServer.getLogger().error("Cannot load server config.",e);
            ButtyServer.getLogger().error("Please check your config file.");
        }
    }

    public int getPort() {
        return port;
    }

    public String[] getMotd() {
        return motd;
    }
}
