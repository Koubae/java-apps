package com.koubae.app.terminal;

public enum Commands {
    DEFAULT(""),
    DIR("/dir"),
    LS("/ls"),
    PWD("/pwd")
    ;

    private final String command;

    Commands(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    static public Commands getOrDefault(String input) {
        input = input.toLowerCase();
        for (Commands value: Commands.values()) {
            if (value.command.equals(input)) {
                return value;
            }
        }

        return DEFAULT;
    }

    static public void printValues() {
        Commands[] commands = Commands.values();
        for (Commands command: commands) {
            if (command.toString().trim().isEmpty())
                continue;
            System.out.printf("- %s\n", command);
        }
    }

}
