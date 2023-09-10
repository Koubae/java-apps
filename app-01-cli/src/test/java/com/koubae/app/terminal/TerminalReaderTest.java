package com.koubae.app.terminal;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TerminalReaderTest {

    @Test
    void getInputString() {
        String expected = "Hello World";
        String result = "";
        try (InputStream userInput = new ByteArrayInputStream(expected.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input();
        } catch (IOException e) {
            System.err.printf("Error -> %s", e);
        }

        assertEquals(result, expected);

    }

    @Test
    void getInputStringWithMessage() {
        String value = "Hello";
        String result = "";
        try (InputStream userInput = new ByteArrayInputStream(value.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input("Write Something:\n");
        } catch (IOException e) {
            System.err.printf("Error -> %s", e);
        }

        assertNotNull(result);

    }

    @Test
    void getInputInt() {
        String expected = "10";
        int result = -1;
        try (InputStream userInput =  new ByteArrayInputStream(expected.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input(0);
        } catch (IOException e) {
            System.err.printf("Error -> %s", e);
        }

        assertEquals(result, Integer.parseInt(expected));

    }

    @Test
    void getInputIntWithMessage() {
        String expected = "10";
        int result = -1;
        try (InputStream userInput = new ByteArrayInputStream(expected.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input("Write integer:\n", 0);
        } catch (IOException e) {
            System.err.printf("Error -> %s", e);
        }

        assertEquals(result, Integer.parseInt(expected));
    }

    @Test
    void getInputBoolean() {
        String expected = "true";
        boolean result = false;
        try (InputStream userInput = new ByteArrayInputStream(expected.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input(true);
        } catch (IOException e) {
            System.err.printf("Error -> %s\n", e);
        }

        assertEquals(result, Boolean.parseBoolean(expected));

    }

    @Test
    void getInputBooleanWithMessage() {
        String expected = "true";
        boolean result = false;
        try (InputStream userInput = new ByteArrayInputStream(expected.getBytes())) {
            TerminalReader reader = new TerminalReader(userInput);
            result = reader.input("Write boolean:\n",true);
        } catch (IOException e) {
            System.err.printf("Error -> %s\n", e);
        }

        assertEquals(result, Boolean.parseBoolean(expected));

    }

}
