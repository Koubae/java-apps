package com.koubae.app.terminal;

import com.koubae.app.App;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {
    private final Window window = new Window(new App());
    private final CommandManager commands = new CommandManager(window);

    private final PrintStream stdOut = System.out;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    void tearDown() {
        System.setOut(stdOut);
    }

    @Test
    void actionReturnsContinueIfCommandNotFound() throws CommandManager.CommandManagerQuitException {
        String userInput = "/wrongValue";

        Action action = commands.action(userInput);

        assertEquals(action, Action.CONTINUE);
    }

    @Test
    void actionReturnsHelp() throws CommandManager.CommandManagerQuitException {
        String userInput = "/help";

        Action action = commands.action(userInput);

        assertEquals(action, Action.HELP);
    }

    @Test
    void actionOnExitRaises() {
        assertThrows(CommandManager.CommandManagerQuitException.class, () -> {commands.action("/quit");});
        assertThrows(CommandManager.CommandManagerQuitException.class, () -> {commands.action("/exit");});
    }

    @Test
    void actionLs() throws CommandManager.CommandManagerQuitException {
        String userInput = "/ls";

        Action action = commands.action(userInput);

        assertTrue(getStandardOut().contains("pom.xml"));
        assertEquals(action, Action.LS);
    }

    private String getStandardOut() {
        System.out.flush();
        System.setOut(stdOut);
        return output.toString();
    }

}