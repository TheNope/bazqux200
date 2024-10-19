package com.thenope.bazqux200.util;

import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;
import com.thenope.bazqux200.music.Playlist;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class ObservablePlaylists {
    public static ObservableList<Playlist> getObservablePlaylists() {
        Path libraryLocation = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getLibraryConfig().getLocation();
        ArrayList<Path> playlistPaths = DirectorySearch.findPlaylists(libraryLocation);
        ArrayList<Playlist> playlists = new ArrayList<Playlist>(0);

        for(int i = 0; i < playlistPaths.toArray().length; i++) {
            try {
                playlists.add(new Playlist((Path) playlistPaths.toArray()[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FXCollections.observableArrayList(playlists);
    }
}
