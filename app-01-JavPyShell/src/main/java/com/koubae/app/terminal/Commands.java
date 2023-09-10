package com.koubae.app.terminal;

public enum Commands {
    DIR("/dir"),
    LS("/ls")
    ;

    private final String command;

    Commands(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

}
