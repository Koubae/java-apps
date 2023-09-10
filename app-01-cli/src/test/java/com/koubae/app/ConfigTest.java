package com.koubae.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    public void givenAppConfiguration_WhenLoaded_ThenReadsProperties() throws Config.ConfigException {
        Config configuration = new Config();

        assertNotNull(configuration.getAppName());
        assertNotNull(configuration.getAppVersion());
    }
}
