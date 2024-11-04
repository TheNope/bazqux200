package com.thenope.bazqux200.config.classes;

import java.nio.file.Path;

public class CondenseConfig {
    private Path location;
    private boolean compress;

    public Path getLocation() {
        return location;
    }

    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    public boolean getCompress() { return compress; }

    public void setLocation(String location) {
        this.location = Path.of(location);
    }
}
