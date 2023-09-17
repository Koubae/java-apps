package com.koubae.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;

public final class Config {
    public static class ConfigException extends Exception {
        public ConfigException(String errorMessage) {
            super(errorMessage);
        }
    }

    public enum OS {
        UNKNOWN,
        WIN,
        MAC,
        UNIX,
        SOLARIS
    }

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-1s] %5$s %n%6$s%n");
    }

    Config() throws ConfigException {
        Properties properties = loadApplicationProperties();

        appName = properties.getProperty("app.name", null);
        appVersion = properties.getProperty("app.version", null);
        terminalCommandStackSize = Integer.parseInt(properties.getProperty("app.terminal.command.stack.size",  "100"));
        sysOS = determineOS();
        if (sysOS != OS.WIN && sysOS != OS.UNIX) {
            throw new ConfigException(String.format(
                    "Operating system %s is not supported, currently only Windows or Linux are", System.getProperty("os.name")));
        }
    }

    private final OS sysOS;
    private final String appName;
    private final String appVersion;
    private final int terminalCommandStackSize;

    public OS getSysOS() {
        return sysOS;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public int getTerminalCommandStackSize() {
        return terminalCommandStackSize;
    }

    private Properties loadApplicationProperties() throws ConfigException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream("application.properties")) {
            properties.load(resourceStream);
        } catch (IOException | NullPointerException error) {
            logger.log(Level.SEVERE, "Error while loading application.properties", error);
            throw new ConfigException("application.properties could not be loaded, error " + error);
        }

        return properties;
    }

    private OS determineOS() {
        String systemOS = System.getProperty("os.name").toLowerCase();
        logger.info(String.format("Running On %s\n", systemOS));
        if (systemOS.contains("win")) {
            return OS.WIN;
        } else if (systemOS.contains("mac")) {
            return OS.MAC;
        } else if (systemOS.contains("nix") || systemOS.contains("nux") || systemOS.contains("aix")) {
            return OS.UNIX;
        } else if (systemOS.contains("sunos")) {
            return OS.SOLARIS;
        } else {
            return OS.UNKNOWN;
        }

    }

}
