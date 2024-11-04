package com.thenope.bazqux200.util;

import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;
import com.thenope.bazqux200.music.Playlist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class ObservablePlaylists {
    public static ArrayList<Playlist> getPlaylists(Path dir) {
        Path libraryLocation = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getLibraryConfig().getLocation();
        ArrayList<Path> playlistPaths = DirectorySearch.findPlaylists(dir);
        ArrayList<Playlist> playlists = new ArrayList<>(0);

        for (int i = 0; i < playlistPaths.toArray().length; i++) {
            playlists.add(new Playlist(libraryLocation, (Path) playlistPaths.toArray()[i]));
        }
        return playlists;
    }

    public static ObservableList<Playlist> getObservablePlaylists(Path dir) {
        return FXCollections.observableArrayList(getPlaylists(dir));
    }
}
