package com.koubae.app.terminal;

import com.koubae.app.Config;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PythonShell {
    private static final int COMMAND_QUEUE_MAX_SIZE = 100;
    private static final Logger logger = Logger.getLogger(CommandManager.class.getName());
    private final Window window;

    private final Config.OS sysOS;
    private final String[] shell;
    private Process process;
    private ArrayBlockingQueue<String> queue;

    public PythonShell(Window window) {
        this.window = window;
        sysOS = window.getApp().os();
        shell = switch (sysOS) {
            case WIN -> new String[]{"python", "-i"};
            case UNIX -> new String[]{"python3", "-i"};
            default -> null;
        };

        queue = new ArrayBlockingQueue<>(COMMAND_QUEUE_MAX_SIZE);

        startPython();
    }

    public void close() {
        stopPython();
    }

    public void run(String statement) {
        try {
            queue.put(statement);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startPython() {
        try {
            createShellProcess();
        } catch (IOException error) {
            logger.log(Level.SEVERE, "Exception while starting python Interpreter", error);
            System.exit(1);
        }
        threadPipeOut();
        threadPipeIn();

    }

    private void stopPython()  {
        if (process != null && process.isAlive()) {
            process.destroy();
        }
        process = null;
    }

    private void createShellProcess() throws IOException {
        stopPython();
        ProcessBuilder builder = new ProcessBuilder(shell);
        builder.redirectErrorStream(true);
        process = builder.start();
    }

    private void threadPipeIn() {
        Thread job = new Thread(() -> {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            while (true) {
                try {
                    String command = queue.take();
                    writer.write(command);
                    writer.newLine();
                    writer.flush();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        job.setDaemon(true);
        job.start();
    }

    private void threadPipeOut() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        Thread job = new Thread(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line.replace(">>> ", "").replace("... ", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        job.setDaemon(true);
        job.start();
    }


}
