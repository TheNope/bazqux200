package com.thenope.bazqux200.config.classes;

import java.nio.file.Path;

public class LibraryConfig {
    private Path location;
    private Path condensedLocation;

    public Path getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = Path.of(location);
    }

    public Path getCondensedLocation() {
        return condensedLocation;
    }

    public void setCondensedLocation(String condensedLocation) {
        this.condensedLocation = Path.of(condensedLocation);
    }
}
