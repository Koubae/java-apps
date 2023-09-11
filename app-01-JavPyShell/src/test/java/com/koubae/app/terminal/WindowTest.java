package com.koubae.app.terminal;

import com.koubae.app.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unittesting anything in System.out / System.in is pretty cumbersome and for this small app is doing not worth it
 * There is probably a good / easy way to do it but I leave it for the time being
 *
 * These tests are actually not valid because, when we fire the threadCreateWindow method we don't wait (Thread.join) for the thread to finish
 * at it kills the window (so is not valid for test where we check if the window can quit with command /quit and so on),
 * @see <a href="https://stackoverflow.com/questions/1647907/junit-how-to-simulate-system-in-testing">JUnit: How to simulate System.in testing?</a>
 */
class WindowTest {
    private final InputStream stdIn = System.in;

    private ByteArrayInputStream stubIn;


    @BeforeEach
    void setUp() {
        System.setProperty("sys.app.welcome", "false");
    }

    @AfterEach
    void tearDown() {
        System.setIn(stdIn);
    }

    private void writeToStdIn(String input) {
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
        synchronized (System.in) {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
        }
    }

    @Test
    void windowCloseOnQuitCommand() throws InterruptedException {
        synchronized (System.in) {
            System.setIn(new ByteArrayInputStream("/quit".getBytes()));
        }
        Window window = threadCreateWindow();

        Thread.sleep(10);
        assertFalse(window.isRunning());
    }

    @Test
    void windowCloseOnExitCommand() throws InterruptedException {
        synchronized (System.in) {
            System.setIn(new ByteArrayInputStream("/exit".getBytes()));
        }
        Window window = threadCreateWindow();

        Thread.sleep(10);
        assertFalse(window.isRunning());
    }

    @Test
    void windowPrintsHelp() {
        threadCreateWindow();
        writeToSystemInToQuitWithDelay(1000);
    }

    /*
    *
    *
    * This is not working
    @Test
    void helpCommand() throws InterruptedException {
//        for (int i = 0; i < 5; i++) {
//            writeToStdIn("/help");
//        }

        Window window = new Window(new App());
        Thread t = new Thread(() -> {
            try {
                window.start();
            } catch (Window.WindowException error) {
                System.err.printf("Error occurred %s\n", error);
            }
        });
        t.start();
        writeToStdIn("/help");
        System.setIn(stdIn);

//        for (int i = 0; i < 5; i++) {
//            writeToStdIn("/help");
//        }
//        t.join();

    }

    * */
    private Window threadCreateWindow() {
        Window window = new Window(new App());
        new Thread(() -> {
            try {
                window.start();
            } catch (Window.WindowException error) {
                System.err.printf("Error occurred %s\n", error);
            }
        }).start();
        return window;
    }

    private void threadWriteToInput(String userInput) {
        new Thread(() -> {
            synchronized (System.in) {
                System.setIn(new ByteArrayInputStream(userInput.getBytes()));
            }
        }).start();
    }

    private void writeToSystemInToQuitWithDelay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }

        System.setIn(new ByteArrayInputStream("/quit".getBytes()));
    }
}