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
    private final static Logger logger =  Logger.getLogger(Config.class.getName());

    public App() {
        logger.info(String.format("AppName=%s", configuration.getAppName()));
    }

}
