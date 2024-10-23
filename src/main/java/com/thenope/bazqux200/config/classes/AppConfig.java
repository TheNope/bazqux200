package com.thenope.bazqux200.config.classes;

public class AppConfig {
    private PlayerConfig player;
    private LibraryConfig library;

    public PlayerConfig getPlayerConfig() {
        return player;
    }

    public void setPlayerConfig(PlayerConfig player) {
        this.player = player;
    }

    public LibraryConfig getLibraryConfig() {
        return library;
    }

    public void setLibraryConfig(LibraryConfig library) {
        this.library = library;
    }
}
