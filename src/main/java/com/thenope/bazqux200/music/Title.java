package com.thenope.bazqux200.music;

import com.thenope.bazqux200.util.PlayingState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class Title {
    private Path path;
    private ObjectProperty<PlayingState> playingState;
    private StringProperty name;

    public Title(Path titlePath) {
        path = titlePath;
        name = new SimpleStringProperty(titlePath.toString());
        playingState = new SimpleObjectProperty<>(PlayingState.INACTIVE);
    }

    public String getPlayingState() {
        return playingState.toString();
    }

    public ObjectProperty<PlayingState> playingStateProperty() {
        return playingState;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void play() {
        this.playingState = new SimpleObjectProperty<>(PlayingState.PLAYING);
    }

    public void pause() {
        this.playingState = new SimpleObjectProperty<>(PlayingState.PAUSED);
    }

    public void setInactive() {
        this.playingState = new SimpleObjectProperty<>(PlayingState.INACTIVE);
    }
}
