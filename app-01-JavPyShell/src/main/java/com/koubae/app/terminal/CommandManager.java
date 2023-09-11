package com.koubae.app.terminal;

public class CommandManager {
    private final Window window;

    public CommandManager(Window window) {
        this.window = window;
    }

    public Action action(String userInput) throws CommandManagerQuitException {
        String userInputCommand = userInput.toLowerCase();
        Action actionCommands = readNextCommand(userInputCommand);
        switch (actionCommands) {
            case HELP:
                window.help();
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

    private Action readNextCommand(String userInput) throws CommandManagerQuitException {
        userInput = userInput.toLowerCase();
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

}
