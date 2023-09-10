package com.koubae.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AppTest {

    @Test
    void appLoadConfigurations() {
        App app = new App();
        assertNotNull(app.name());

    }

}