package com.koubae.app.terminal;

import com.koubae.app.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class WindowTest {

    @Test
    void welcomeMessageWontPrintIfSystemSettingsIsOff() {
        System.setProperty("sys.app.welcome", "false");
        Window window = new Window(new App());
        try {
            window.start();
        } catch (Window.WindowException error) {
            System.err.printf("Error occurred %s\n", error);
        }
    }
}