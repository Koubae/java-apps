package com.koubae.app.terminal;

import java.io.InputStream;
import java.util.Scanner;

public class TerminalReader {
    private final Scanner scanner;

    TerminalReader() {
        scanner = new Scanner(System.in);
    }

    TerminalReader(InputStream streamer) {
        scanner = new Scanner(streamer);
    }

    public String input() {
        return _inputString();
    }

    public int input(int ignored_) {
        return _inputInt();
    }

    public boolean input(boolean ignored_) {
        return _inputBoolean();
    }

    public String input(String message) {
        _printMessage(message);
        return _inputString();
    }

    public int input(String message, int ignored_) {
        _printMessage(message);
        return _inputInt();
    }

    public boolean input(String message, boolean ignored_) {
        _printMessage(message);
        return _inputBoolean();
    }

    private String _inputString() {
        String output = "";
        if (scanner.hasNextLine()) {
            output = scanner.nextLine();
        }
        return output;
    }

    private int _inputInt() {
        int output = 0;
        if (scanner.hasNextInt()) {
            output = scanner.nextInt();
        }
        return output;
    }

    private boolean _inputBoolean() {
        boolean output = false;
        if (scanner.hasNextBoolean()) {
            output = scanner.nextBoolean();
        }
        return output;
    }

    private void _printMessage(String message) {
        System.out.print(message);
    }

}
