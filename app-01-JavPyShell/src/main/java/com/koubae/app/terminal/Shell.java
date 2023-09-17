package com.koubae.app.terminal;

import com.koubae.app.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Shell {
    private static final Logger logger = Logger.getLogger(CommandManager.class.getName());

    private final Config.OS sysOS;
    private final String[] shell;
    private Process shellProcess = null;

    public Shell(Config.OS operetingSystem) {
        sysOS = operetingSystem;
        shell = switch (sysOS) {
            case WIN -> new String[]{"CMD", "/C"};
            case UNIX -> new String[]{"bash", "-c"};
            default -> null;
        };
    }

    public void ls() {
        executeShellCommand(switch (sysOS) {
            case WIN -> "dir";
            case UNIX -> "ls -lah";
            default -> null;
        });
    }

    public void pwd() {
        executeShellCommand(switch (sysOS) {
            case WIN -> "echo %cd%";
            case UNIX -> "pwd";
            default -> null;
        });
    }

    private void executeShellCommand(String command) {
        CommandOutput output = runShellCommand(command);
        System.out.println(output);
    }

    private CommandOutput runShellCommand(String command) {
        StringBuilder output = new StringBuilder();
        int exitVal = -1;
        try {

            shellCommandOnStart(command);
            readProcessOutput(shellProcess, output);
            exitVal = shellProcess.waitFor();

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
            shellCommandOnEnd();
        }

        return new CommandOutput(command, output.toString(), exitVal);

    }

    protected void shellCommandOnStart(String command) throws IOException {
        createShellProcess(command);
    }

    protected void shellCommandOnEnd()  {
        if (shellProcess != null && shellProcess.isAlive()) {
            shellProcess.destroy();
        }
        shellProcess = null;
    }

    private void createShellProcess(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder((String[]) Stream.of(shell, command).toArray(java.io.Serializable[]::new));
        builder.redirectErrorStream(true);
        shellProcess = builder.start();
    }

    private void readProcessOutput(Process subProcess, StringBuilder output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(subProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
    }

}
