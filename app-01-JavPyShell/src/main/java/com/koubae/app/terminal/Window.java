package com.koubae.app.terminal;

import com.koubae.app.App;
import com.koubae.app.Config;
import com.koubae.app.Constants;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Window {
    private static final int WELCOME_WAIT_MIN = 1;
    private static final int WELCOME_WAIT_MAX = 85;

    private final App app;
    private final TerminalReader terminal;
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    public Window(App application) {
        app = application;
        terminal = new TerminalReader();
    }

    public static class WindowException extends Exception {
        public WindowException(String errorMessage) {
            super(errorMessage);
        }
    }

    public void start() throws WindowException {
        welcome();
        loop();
    }

    private void loop() throws WindowException {
        boolean running = true;
        try {
            while (running) {
                String userInput = terminal.input(">>> ");
                System.out.printf("User wrote -> %s\n", userInput);
                if (Objects.equals(userInput, CommandsBase.EXIT.toString()) || Objects.equals(userInput, CommandsBase.QUIT.toString())) {
                    running = false;
                    // todo: write goodbye here
                }

            }

        } catch (Exception error) {
            logger.log(Level.SEVERE, String.format("Window loop interrupted with an error %s", error), error);
            throw new WindowException(String.format("Window loop had an error (%s), exiting", error));
        }
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

        System.out.print(Constants.WELCOME_HELP_COMMAND);
        CommandsBase[] commands = CommandsBase.values();
        for (CommandsBase c: commands) {
            System.out.printf("- %s\n", c);
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
