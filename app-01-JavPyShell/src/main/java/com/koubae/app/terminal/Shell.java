package com.koubae.app.terminal;

import com.koubae.app.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shell {
    private static final Logger logger = Logger.getLogger(CommandManager.class.getName());

    private final Config.OS sysOS;
    private final String[] shell;

    public Shell(Config.OS operetingSystem) {
        sysOS = operetingSystem;
        shell = switch (sysOS) {
            case WIN -> new String[]{"CMD", "/C"};
            case UNIX -> new String[]{"bash", "-c"};
            default -> null;
        };
    }

    public void ls() {
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
