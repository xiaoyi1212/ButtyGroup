package io.butty.craftbutty.util.config;

import java.util.List;

public class SimpleServerConfig {
    int port;
    String motd;
    String version;
    List<Server> servers;

    public void setPort(int port) {
        this.port = port;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public int getPort() {
        return port;
    }

    public String getMotd() {
        return motd;
    }

    public String getVersion() {
        return version;
    }

    public List<Server> getServers() {
        return servers;
    }

    public static class Server {
        String name;
        String ip;
        int port;

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIp() {
            return ip;
        }

        public int getPort() {
            return port;
        }

        public String getName() {
            return name;
        }
    }
}
