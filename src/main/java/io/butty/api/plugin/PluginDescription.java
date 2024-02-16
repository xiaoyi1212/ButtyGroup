package io.butty.api.plugin;

import io.butty.util.Configuration;
import io.butty.util.YamlConfiguration;

import java.io.InputStream;
import java.util.List;

public class PluginDescription {
    String name;
    String version;
    String website;
    String main;
    List<String> authors;

    public PluginDescription(InputStream in){
        Configuration configuration = new YamlConfiguration().load(in);
        this.name = configuration.getString("name");
        this.main = configuration.getString("main");
        this.version = configuration.getString("version") == null ? "0.0.1" : configuration.getString("version");
        this.website = configuration.getString("website") == null ? "https://www.example.com" : configuration.getString("website");
        this.authors = configuration.getStringList("authors");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getWebsite() {
        return website;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getMain() {
        return main;
    }
}
