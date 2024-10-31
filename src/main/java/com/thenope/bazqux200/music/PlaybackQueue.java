package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.util.PlayingMode;
import com.thenope.bazqux200.util.PlayingState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlaybackQueue {
    private ArrayList<Title> queue;
    private ArrayList<Title> shuffledQueue;
    private Title currentTitle;
    private Integer currentTitleIndex;
    private PlayingState playingState;
    private PlayingMode playingMode;

    public PlaybackQueue() {
        queue = new ArrayList<>(0);
        shuffledQueue = new ArrayList<>(0);
        currentTitle = null;
        currentTitleIndex = null;
        playingState = PlayingState.INACTIVE;
        playingMode = PlayingMode.REPEAT;
    }

    public ArrayList<Title> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Title> newQueue) {
        if (isReady() && (currentTitle != null)) {
            currentTitle.setPlayingState(PlayingState.INACTIVE);
        }
        this.queue = newQueue;
        this.currentTitleIndex = 0;
        this.playingState = PlayingState.PAUSED;
    }

    public Title getCurrentTitle() {
        return currentTitle;
    }

    public String getPlayingMode() {
        return playingMode.toString();
    }

    public void nextPlayingMode() {
        switch (playingMode) {
            case PlayingMode.REPEAT:
                playingMode = PlayingMode.RANDOM;
                shuffledQueue = new ArrayList<>(queue);
                Collections.shuffle(shuffledQueue);
                playingMode = PlayingMode.SHUFFLE;
                break;
            case PlayingMode.SHUFFLE:
                shuffledQueue = new ArrayList<>(0);
                playingMode = PlayingMode.RANDOM;
                break;
            case PlayingMode.RANDOM:
                shuffledQueue = new ArrayList<>(0);
                playingMode = PlayingMode.REPEAT;
        }
    }

    public Boolean isReady() {
        return !queue.isEmpty() && (playingState != PlayingState.INACTIVE);
    }

    public Boolean isPlaying() {
        return playingState == PlayingState.PLAYING;
    }

    public void play() {
        if (playingMode != PlayingMode.SHUFFLE) {
            currentTitle = queue.get(currentTitleIndex);
        } else {
            currentTitle = shuffledQueue.get(currentTitleIndex);
        }
        Application.getAudioPlayer().setTitle(currentTitle);
        playingState = PlayingState.PLAYING;
        currentTitle.setPlayingState(playingState);
        Application.getAudioPlayer().play();
        Application.getLogger().info("Now playing: {}", currentTitle.getName());
    }

    public void proceed() {
        playingState = PlayingState.PLAYING;
        currentTitle.setPlayingState(playingState);
        Application.getAudioPlayer().proceed();
    }

    public void pause() {
        playingState = PlayingState.PAUSED;
        currentTitle.setPlayingState(playingState);
        Application.getAudioPlayer().pause();
    }

    public void next() {
        if (playingMode == PlayingMode.RANDOM) {
            Random random = new Random();
            int newCurrentTitleIndex;
            do {
                newCurrentTitleIndex = random.nextInt() % queue.size();
                if(newCurrentTitleIndex < 0) {
                    newCurrentTitleIndex *= -1;
                }
            } while (newCurrentTitleIndex == currentTitleIndex);
            currentTitleIndex = newCurrentTitleIndex;
        } else if (currentTitleIndex < queue.size() - 1) {
            currentTitleIndex++;
        } else {
            currentTitleIndex = 0;
        }
        currentTitle.setPlayingState(PlayingState.INACTIVE);
        play();
    }

    public void previous() {
        if (currentTitleIndex > 0) {
            currentTitleIndex--;
        } else {
            currentTitleIndex = queue.size() - 1;
        }
        currentTitle.setPlayingState(PlayingState.INACTIVE);
        play();
    }

    public void setProgress(Number progress) {
        long titleLength = Application.getAudioPlayer().getLength();
        Application.getAudioPlayer().skipTo(titleLength * progress.longValue() / 100);
    }
}
