package com.thenope.bazqux200.config.classes;

import java.nio.file.Path;

public class LibraryConfig {
    private Path location;

    public Path getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Path.of(location);
    }
}
