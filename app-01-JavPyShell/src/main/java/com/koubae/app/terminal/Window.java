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
    private static final int PYTHON_OUTPUT_WAIT_TIME_MILLISECONDS = 250;

    private final App app;
    private final TerminalReader terminal;
    private final CommandManager commandManager;
    private static final Logger logger = Logger.getLogger(Config.class.getName());

    private boolean running = true;


    public Window(App application) {
        app = application;
        commandManager = new CommandManager(this);
        wait(250);
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

    public boolean isRunning() {
        return running;
    }
    public App getApp() {
        return app;
    }

    public String getNextLineToken() {
        return ">>> ";
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
                try {

                    String userInput = terminal.input(getNextLineToken());
                    Action action = commandManager.action(userInput);
                    waitForPythonOutput(action);

                } catch (NullPointerException ignored) {
                    running = false;
                    System.out.println();
                    logger.info("Received Closing Signal");
                } catch (CommandManager.CommandManagerQuitException  ignored) {
                    running = false;
                }

            }

        } catch (Exception error) {
            logger.log(Level.SEVERE, String.format("Window loop interrupted with an error %s", error), error);
            throw new WindowException(String.format("Window loop had an error (%s), exiting", error));
        } finally {
            goodbye();
        }
    }


    /**
     * Prints a Welcome Message to stdout with delay, faking the pace as if someone is really typing it
     */
    private void welcome() {
        if (!app.getConfiguration().getAppWelcome()) {
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

    private void goodbye() {
        commandManager.close();
        terminal.close();
        System.out.printf(Constants.GOODBYE_MESSAGE, app.name());
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

    private void waitForPythonOutput(Action action) {
        if (action.equals(Action.RUN_PY))
            wait(PYTHON_OUTPUT_WAIT_TIME_MILLISECONDS);
    }

    private int randomIntegerRange() {
        return ThreadLocalRandom.current().nextInt(WELCOME_WAIT_MIN, WELCOME_WAIT_MAX + 1);
    }
}
