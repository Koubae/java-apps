package com.koubae.app.terminal;

import com.koubae.app.App;
import com.koubae.app.Config;
import com.koubae.app.Constants;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Window {
    private static final int WELCOME_WAIT_MIN = 1;
    private static final int WELCOME_WAIT_MAX = 85;

    private final App app;
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    public Window(App application) {
        app = application;
    }

    public void start() {
        welcome();
    }

    /**
     * Prints a Welcome Message to stdout with delay, faking the pace as if someone is really typing it
     */
    private void welcome() {
        if (!Boolean.parseBoolean(System.getProperty("sys.app.welcome", "true"))) {
            return;
        }
        System.out.printf("%s", Constants.WELCOME_BANNER);
        String welcomeMessage = String.format(Constants.WELCOME_MESSAGE, app.name());

        for (int i = 0; i < welcomeMessage.length(); i++) {
            char character = welcomeMessage.charAt(i);
            if (character == ',') {
                System.out.print(character);
                wait(650);
                continue;
            }

            wait(randomIntegerRange());
            System.out.print(character);
        }
    }

    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored_) {
            Thread.currentThread().interrupt();
        }
    }

    private int randomIntegerRange() {
        return ThreadLocalRandom.current().nextInt(WELCOME_WAIT_MIN, WELCOME_WAIT_MAX + 1);
    }
}
