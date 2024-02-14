package io.butty.craftbutty.util.config;

import io.butty.craftbutty.util.Util;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class Config {
    File config;
    Yaml yaml;
    SimpleServerConfig config_simple;

    public Config(){
        this.yaml = new Yaml();
    }

    public void loadConfig(File config) throws IOException {
        if(!config.exists()){
            config.createNewFile();
            try(BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(config))){
                byte[] b = new byte[1024];
                int length;
                InputStream is = Util.getResource("config.yml");
                while((length = is.read(b))>0)
                    out.write(b,0,length);
            }catch (IOException e){
                throw e;
            }
        }
        this.config = config;
        config_simple = yaml.loadAs(new FileInputStream(config), SimpleServerConfig.class);
    }

    public SimpleServerConfig getConfig(){
        return config_simple;
    }
}
