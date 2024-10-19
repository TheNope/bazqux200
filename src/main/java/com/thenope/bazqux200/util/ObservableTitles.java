package com.thenope.bazqux200.util;

import com.thenope.bazqux200.music.Playlist;
import com.thenope.bazqux200.music.Title;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableTitles {
    public static ObservableList<Title> getObservableTitles(Playlist playlist) {
        return FXCollections.observableArrayList(playlist.getContent());
    }
}
