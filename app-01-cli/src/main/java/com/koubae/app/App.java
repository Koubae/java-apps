package com.koubae.app;

import java.util.logging.Logger;

public class App {

    private static Config configuration = null;

    static {
        try {
            configuration = new Config();
        } catch (Config.ConfigException error) {
            System.exit(1);
        }
    }

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    public App() {}

    public void run() {
        System.out.printf("%s", Constants.WELCOME_BANNER);
        System.out.printf(String.format(Constants.WELCOME_MESSAGE, name()));
    }

    public String name() {
        return configuration.getAppName();
    }
}
