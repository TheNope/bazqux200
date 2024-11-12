package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.util.PlayingMode;
import com.thenope.bazqux200.util.PlayingState;
import com.thenope.bazqux200.util.Random;

import java.util.ArrayList;
import java.util.Collections;

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

    public void setQueue(PlaybackQueue newQueue) {
        if (isReady() && (currentTitle != null)) {
            currentTitle.setPlayingState(PlayingState.INACTIVE);
        }
        playingMode = newQueue.getPlayingMode();
        queue = newQueue.getQueue();
        currentTitleIndex = 0;
        switch (playingMode) {
            case PlayingMode.SHUFFLE:
                shuffledQueue = new ArrayList<>(queue);
                Collections.shuffle(shuffledQueue);
                break;
            case PlayingMode.RANDOM:
                currentTitleIndex = Random.RandomPositiveInt(currentTitleIndex, queue.size());
                break;
        }
        playingState = PlayingState.PAUSED;
    }

    public void setQueue(ArrayList<Title> newQueue) {
        this.queue = newQueue;
    }

    public Title getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(Title newCurrentTitle) {
        if (currentTitle != null) {
            currentTitle.setPlayingState(PlayingState.INACTIVE);
        }
        int newCurrentTitleIndex;
        if (playingMode != PlayingMode.SHUFFLE) {
            newCurrentTitleIndex = queue.indexOf(newCurrentTitle);
        } else {
            newCurrentTitleIndex = shuffledQueue.indexOf(newCurrentTitle);
        }
        currentTitleIndex = newCurrentTitleIndex;
    }

    public PlayingMode getPlayingMode() {
        return playingMode;
    }

    public String getStringPlayingMode() {
        return playingMode.toString();
    }

    public void nextPlayingMode() {
        switch (playingMode) {
            case PlayingMode.REPEAT:
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
            currentTitleIndex = Random.RandomPositiveInt(currentTitleIndex, queue.size());
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
