package com.koubae.app.terminal;


public class CommandManager {
    private final Window window;
    private final Shell shell;
    private final PythonShell pythonShell;

    public CommandManager(Window window) {
        this.window = window;
        this.shell = new Shell(window.getApp().os());
        this.pythonShell = new PythonShell(window.getApp().os());
    }

    public Action action(String userInput) throws CommandManagerQuitException {
        String userInputCommand = userInput.toLowerCase();
        Action actionCommands = readNextAction(userInputCommand);
        switch (actionCommands) {
            case HELP:
                window.help();
                break;
            case LS:
                shell.ls();
                break;
            case PWD:
                shell.pwd();
                break;
            case RUN_PY:
                pythonShell.run(userInputCommand);
                break;
            default:
                break;

        }
        return actionCommands;
    }

    public void close() {
        pythonShell.close();
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

    private Action isPythonCommand(String ignored) {
        return Action.RUN_PY;
    }

}
