package com.koubae.app.terminal;

public enum CommandsBase {
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

}
