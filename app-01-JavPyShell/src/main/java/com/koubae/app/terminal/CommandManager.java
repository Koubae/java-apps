package com.koubae.app.terminal;

import com.koubae.app.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommandManager {
    private final Window window;
    private static final Logger logger = Logger.getLogger(CommandManager.class.getName());
    private final String[] shell;
    private final Config.OS sysOS;

    public CommandManager(Window window) {
        this.window = window;
        sysOS = window.getApp().os();
        shell = switch (sysOS) {
            case WIN -> new String[]{"CMD", "/C"};
            case UNIX -> new String[]{"bash", "-c"};
            default -> null;
        };

    }

    public Action action(String userInput) throws CommandManagerQuitException {
        String userInputCommand = userInput.toLowerCase();
        Action actionCommands = readNextAction(userInputCommand);
        switch (actionCommands) {
            case HELP:
                window.help();
                break;
            case LS:
                System.out.printf("Action %s is a LS command!\n", userInput);
                ls();
                break;
            case PWD:
                System.out.printf("Action %s is a PWD command!\n", userInput);
                break;
            case RUN_PY:
                System.out.printf("Action %s is a python command!\n", userInput);
                break;
            default:
                break;

        }
        return actionCommands;
    }

    public static class CommandManagerException extends Exception {
        public CommandManagerException(String errorMessage) { super(errorMessage); }
    }

    public static class CommandManagerQuitException extends CommandManagerException {
        public CommandManagerQuitException(String errorMessage) {
            super(errorMessage);
        }
    }

    private Action readNextAction(String userInput) throws CommandManagerQuitException {
        userInput = userInput.toLowerCase();
        Action action = readCommand(userInput);
        if (action == Action.CONTINUE)
            action = isPythonCommand(userInput);

        return action;

    }

    private Action readCommand(String userInput) throws CommandManagerQuitException {
        Action baseAction = switch (CommandsBase.getOrDefault(userInput)) {
            case EXIT, QUIT -> throw new CommandManagerQuitException(String.format("User quit : %s", userInput));
            case HELP -> Action.HELP;
            default -> Action.CONTINUE;
        };
        if (baseAction != Action.CONTINUE)
            return baseAction;

        return switch (Commands.getOrDefault(userInput)) {
            case DIR, LS -> Action.LS;
            case PWD -> Action.PWD;
            default -> Action.CONTINUE;
        };
    }

    private Action isPythonCommand(String userInput) {
        return Action.CONTINUE;
    }

    private void ls() {
        String lsCommand = switch (sysOS) {
            case WIN -> "dir";
            case UNIX -> "ls -lah";
            default -> null;
        };
        executeShellCommand(lsCommand);
    }

    private void executeShellCommand(String command) {
        CommandOutput output = runShellCommand(command);
        System.out.println(output);
    }

    private CommandOutput runShellCommand(String command) {
        Process subProcess = null;
        StringBuilder output = new StringBuilder();
        int exitVal = -1;
        try {

            ProcessBuilder builder = new ProcessBuilder(shell[0], shell[1], command);
            builder.redirectErrorStream(true);
            subProcess = builder.start();

            readProcessOutput(subProcess, output);
            exitVal = subProcess.waitFor();

        } catch (InterruptedException error) {
            output.insert(0, String.format("InterruptedException while running command %s, error %s", command, error));
            logger.log(Level.SEVERE, String.format("InterruptedException while running command %s", command), error);
        } catch (IOException error) {
            output.insert(0, String.format("IOException while running command %s, error %s", command, error));
            logger.log(Level.SEVERE, String.format("IOException while running command %s", command), error);
        } catch (Exception error) {
            output.insert(0, String.format("Exception while running command %s, error %s", command, error));
            logger.log(Level.SEVERE, String.format("Exception while running command %s", command), error);
        } finally {
            if (subProcess != null && subProcess.isAlive()) {
                subProcess.destroy();
            }
        }

        return new CommandOutput(command, output.toString(), exitVal);

    }

    private void readProcessOutput(Process subProcess, StringBuilder output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(subProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
    }


}
