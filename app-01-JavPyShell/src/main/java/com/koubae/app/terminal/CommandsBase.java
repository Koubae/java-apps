package com.koubae.app.terminal;

public enum CommandsBase {
        DEFAULT(""),
        HELP("/help"),
        EXIT("/exit"),
        QUIT("/quit")
    ;

    private final String command;

    CommandsBase(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    static public CommandsBase getOrDefault(String input) {
        input = input.toLowerCase();
        for (CommandsBase value: CommandsBase.values()) {
            if (value.command.equals(input)) {
                return value;
            }
        }

        return DEFAULT;
    }

    static public void printValues() {
        CommandsBase[] commands = CommandsBase.values();
        for (CommandsBase command: commands) {
            if (command.toString().trim().isEmpty())
                continue;
            System.out.printf("- %s\n", command);
        }
    }

}
