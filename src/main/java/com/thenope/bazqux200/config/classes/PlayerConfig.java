package com.thenope.bazqux200.config.classes;

import com.thenope.bazqux200.Application;

import java.net.URL;

public class PlayerConfig {
    private int initialVolume;
    private String theme;

    public int getInitialVolume() { return initialVolume; }

    public void setInitialVolume(int initialVolume) { this.initialVolume = initialVolume; }

    public URL getTheme() {
        return switch (theme) {
            case "light" -> Application.class.getResource("css/light.css");
            default -> Application.class.getResource("css/dark.css");
        };
    }

    public void setTheme(String theme) { this.theme = theme; }
}