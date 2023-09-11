package com.koubae.app.terminal;

import com.koubae.app.App;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandManagerTest {
    private final Window window = new Window(new App());
    private final CommandManager commands = new CommandManager(window);

    @BeforeEach
    void setUp() {
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


}