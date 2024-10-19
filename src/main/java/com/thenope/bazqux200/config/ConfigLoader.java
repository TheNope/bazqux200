package com.thenope.bazqux200.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigLoader {
    private Path configPath;

    public void setConfigPath() {
        this.configPath = Path.of("config/config.json");
    }

    public <T> T load(Class<T> configClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(configPath.toString()), configClass);
    }

    public static <T> T getConfig(Class<T> configClass) {
        ConfigLoader configLoader = new ConfigLoader();
        configLoader.setConfigPath();
        try {
            return configLoader.load(configClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
