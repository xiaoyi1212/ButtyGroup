package io.butty;

public class ServerJVMChecker {
    public static void check(){
        int v = getJavaVersion();
        if (v < 17) {
            System.err.println("[ERROR]: ButtyGroup requires running the server with Java 17.");
            System.err.println("[ERROR]: You can check your Java version with the command: java -version");
            System.exit(-1);
        }

        System.out.println("Java " + System.getProperty("java.version") + '(' + System.getProperty("java.vm.name") + ')');
        System.out.println("OS Version: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));
    }

    private static int getJavaVersion() {
        String version = System.getProperty("java.specification.version");
        String[] parts = version.split("\\.");
        if (parts.length == 0) {
            throw new IllegalStateException("Could not determine version of the current JVM");
        } else if (parts[0].equals("1")) {
            if (parts.length < 2) {
                throw new IllegalStateException("Could not determine version of the current JVM");
            } else {
                return Integer.parseInt(parts[1]);
            }
        } else {
            return Integer.parseInt(parts[0]);
        }
    }
}
