package com.thenope.bazqux200.config.classes;

public class AppConfig {
    private CondenseConfig condense;
    private PlayerConfig player;
    private LibraryConfig library;

    public CondenseConfig getCondenseConfig() {
        return condense;
    }

    public void setCondenseConfig(CondenseConfig condense) {
        this.condense = condense;
    }

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
