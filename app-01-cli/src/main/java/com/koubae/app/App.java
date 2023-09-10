package com.koubae.app;

import java.util.logging.Logger;

import com.koubae.app.terminal.Window;

public class App {

    private static Config configuration = null;

    static {
        try {
            configuration = new Config();
        } catch (Config.ConfigException error) {
            System.exit(1);
        }
    }

    private final Window window;

    public App() {
        window = new Window(this);
    }

    public void run() {
        window.start();
    }

    public String name() {
        return configuration.getAppName();
    }
}
