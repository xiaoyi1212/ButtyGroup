package io.butty;

import joptsimple.OptionParser;

import java.io.File;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Security.setProperty("networkaddress.cache.ttl", "30");
        Security.setProperty("networkaddress.cache.negative.ttl", "10");
        if (System.getProperty("jdk.util.jar.enableMultiRelease") == null) {
            System.setProperty("jdk.util.jar.enableMultiRelease", "force");
        }
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("h", "port"))
                .withRequiredArg()
                .ofType(Integer.class)
                .defaultsTo(19132);
        parser.acceptsAll(asList("c", "config"))
                .withRequiredArg()
                .ofType(File.class)
                .defaultsTo(new File("config.yml"));

        ServerJVMChecker.check();
        ButtyServer.start(parser.parse(args));
    }

    public static List<String> asList(String... args) {
        return Arrays.asList(args);
    }
}