package com.koubae.app.terminal;


public class CommandManager {
    private final Window window;
    private final Shell shell;

    public CommandManager(Window window) {
        this.window = window;
        this.shell = new Shell(window.getApp().os());
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
                shell.ls();
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

}
