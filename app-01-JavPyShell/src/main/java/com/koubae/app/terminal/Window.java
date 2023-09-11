package com.koubae.app.terminal;

import com.koubae.app.App;
import com.koubae.app.Config;
import com.koubae.app.Constants;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Window {
    private static final int WELCOME_WAIT_MIN = 1;
    private static final int WELCOME_WAIT_MAX = 85;

    private final App app;
    private final TerminalReader terminal;
    private final CommandManager commandManager;
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    private boolean running = true;


    public Window(App application) {
        app = application;
        terminal = new TerminalReader();
        commandManager = new CommandManager(this);
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

    public boolean isRunning() {
        return running;
    }
    public App getApp() {
        return app;
    }

    /**
     * Prints to Console user help
     */
    public void help() {
        System.out.print("\n" + Constants.WELCOME_BANNER);
        System.out.printf((Constants.HELP_TEXT) + "%n" + "\n", app.name());

        printHelpBaseCommand();
        printHelpCommand();
    }

    private void loop() throws WindowException {
        try {
            while (running) {
                String userInput = terminal.input(">>> ");
                System.out.println("Command requested "  + userInput);
                try {
                    commandManager.action(userInput);
                } catch (CommandManager.CommandManagerQuitException ignored_) {
                    running = false;
                    terminal.close();
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
        printHelpBaseCommand();
    }

    private void printHelpBaseCommand() {
        System.out.print(Constants.HELP_COMMAND_BASE);
        CommandsBase.printValues();
        System.out.print("\n");
    }

    private void printHelpCommand() {
        System.out.print(Constants.HELP_COMMAND);
        Commands.printValues();
        System.out.print("\n");
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
