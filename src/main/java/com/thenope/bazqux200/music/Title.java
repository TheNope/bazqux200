package com.thenope.bazqux200.music;

import com.thenope.bazqux200.util.PlayingState;

import java.nio.file.Path;

public class Title {
    private Path path;
    private PlayingState playingState;
    private String name;

    public Title(Path titlePath) {
        path = titlePath;
        name = titlePath.toString();
        playingState = PlayingState.INACTIVE;
    }

    public String getName() {
        return name;
    }

    public void play() {
        playingState = PlayingState.PLAYING;
        System.out.println(name);
    }

    public void pause() {
        playingState = PlayingState.PAUSED;
        System.out.println(name);
    }

    public void setInactive() {
        playingState = PlayingState.INACTIVE;
    }
}
