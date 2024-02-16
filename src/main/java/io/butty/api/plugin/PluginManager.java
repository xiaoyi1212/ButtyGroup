package io.butty.api.plugin;

import io.butty.ButtyServer;
import io.butty.api.event.Event;
import io.butty.api.plugin.java.JPluginImp;
import io.butty.api.plugin.java.JavaPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    File folder;
    List<Plugin> plugins;

    public PluginManager(File folder){
        if(!folder.exists()) folder.mkdir();
        if(!folder.isDirectory())
            throw new IllegalStateException("Cannot found plugin directory.");
        this.folder = folder;
        this.plugins = new ArrayList<>();
    }

    public void callEvent(Event event){

    }

    public void loadJavaPlugins(){
        ButtyServer.getLogger().info("Loading java plugins...");
        for(File plugin:folder.listFiles()){
            JPluginImp plugin_i = loadJavaPlugin(plugin);
            this.plugins.add(plugin_i);
        }
    }

    public JPluginImp loadJavaPlugin(File file){
        try {
            String name = file.getName();
            if (name.split("\\.").length > 0 && name.split("\\.")[1].equals("jar")) {
                JarFile jar = new JarFile(file);

                JarEntry config = jar.getJarEntry("plugin.yml");
                if (config == null) throw new NullPointerException("Not found 'plugin.yml' in jar file.");

                PluginDescription description = new PluginDescription(jar.getInputStream(config));

                URL url1 = new URL("file", "", slashify(file.getAbsolutePath(), file.isDirectory()));
                URLClassLoader pluginClassLoader = new URLClassLoader(new URL[]{url1}, Thread.currentThread()
                        .getContextClassLoader());
                Class<? extends JavaPlugin> mainClass = (Class<? extends JavaPlugin>) pluginClassLoader.loadClass(description.getMain());
                JPluginImp plugin = new JPluginImp(description, mainClass.newInstance(), pluginClassLoader);

                return plugin;
            } else return null;
        }catch (Exception e){
            ButtyServer.getLogger().warn("Cannot load file '{}'",file.getName(),e);
            return null;
        }
    }

    private static String slashify(String path, boolean isDirectory) {
        String p = path;
        if (java.io.File.separatorChar != '/')
            p = p.replace(java.io.File.separatorChar, '/');
        if (!p.startsWith("/"))
            p = "/" + p;
        if (!p.endsWith("/") && isDirectory)
            p = p + "/";
        return p;
    }
}
