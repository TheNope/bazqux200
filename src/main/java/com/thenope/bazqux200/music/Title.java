package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.util.PlayingState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Path;

public class Title {
    private final Path path;
    private ObjectProperty<PlayingState> playingState;
    private StringProperty name;
    private StringProperty track;
    private StringProperty album;
    private StringProperty duration;
    private Image artwork;

    public Title(Path titlePath) {
        try {
            Metadata metadataReader = new Metadata(titlePath);
            name = new SimpleStringProperty(metadataReader.getTitle());
            track = new SimpleStringProperty(metadataReader.getTrack());
            album = new SimpleStringProperty(metadataReader.getAlbum());
            duration = new SimpleStringProperty(metadataReader.getFormattedDuration());
        } catch (Exception e) {
            name = new SimpleStringProperty("");
            track = new SimpleStringProperty("");
            album = new SimpleStringProperty("");
            duration = new SimpleStringProperty("");
        }
        path = titlePath;
        playingState = new SimpleObjectProperty<>(PlayingState.INACTIVE);
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

    public StringProperty trackProperty() {
        return track;
    }

    public StringProperty albumProperty() {
        return album;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public Image imageProperty() {
        if (artwork == null) {
            try {
                Metadata metadataReader = new Metadata(path);
                artwork =  metadataReader.getArtwork();
            } catch(Exception e) {
                Application.getLogger().error(e.getMessage());
            }
        }
        return artwork;
    }

    public Path getPath() {
        return path;
    }

    public void setPlayingState(PlayingState newState) {
        this.playingState = new SimpleObjectProperty<>(newState);
    }
}
