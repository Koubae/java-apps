package com.koubae.app.terminal;

public record CommandOutput(String input, String output, int exitStatus) {
    public CommandOutput {
        if (exitStatus != 0) {
            output = String.format("- Command exited with error status code %d, command input: (%s), output:\n%s", exitStatus, input, output);
        }
    }

    public String toString() {
        return output;
    }
}
