package com.thenope.bazqux200.config.classes;

import java.nio.file.Path;

public class CondenseConfig {
    private Path location;
    private CompressionConfig compressionConfig;

    public Path getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Path.of(location);
    }

    public CompressionConfig getCompressionConfig() { return compressionConfig; }

    public void setCompressionConfig(CompressionConfig compressionConfig) {
        this.compressionConfig = compressionConfig;
    }
}
