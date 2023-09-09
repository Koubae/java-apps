package com.koubae.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Config {
    public static class ConfigException extends Exception {
        public ConfigException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-1s] %5$s %n%6$s%n");
    }

    Config() throws ConfigException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        } catch (IOException | NullPointerException error) {
            logger.log(Level.SEVERE, "Error while loading application.properties", error);
            throw new ConfigException("application.properties could not be loaded, error " + error);
        }

        appName = properties.getProperty("app.name", "NOT_FOUND");
    }

    private final String appName;

    public String getAppName() {
        return appName;
    }
}
