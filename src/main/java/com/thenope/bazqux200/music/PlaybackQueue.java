package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.util.PlayingState;

import java.util.ArrayList;

public class PlaybackQueue {
    private ArrayList<Title> queue;
    private Title currentTitle;
    private Integer currentTitleIndex;
    private PlayingState playingState;

    public PlaybackQueue() {
        queue = new ArrayList<Title>(0);
        currentTitle = null;
        currentTitleIndex = null;
        playingState = PlayingState.INACTIVE;
    }

    public ArrayList<Title> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Title> newQueue) {
        if(isReady() && (currentTitle != null)) {
            currentTitle.setPlayingState(PlayingState.INACTIVE);
        }
        this.queue = newQueue;
        this.currentTitleIndex = 0;
        this.playingState = PlayingState.PAUSED;
    }

    public Title getCurrentTitle() {
        return currentTitle;
    }

    public Boolean isReady() {
        return !queue.isEmpty() && (playingState != PlayingState.INACTIVE);
    }

    public Boolean isPlaying() {
        return playingState == PlayingState.PLAYING;
    }

    public void play() {
        currentTitle = queue.get(currentTitleIndex);
        Application.getAudioPlayer().setTitle(currentTitle);
        playingState = PlayingState.PLAYING;
        currentTitle.setPlayingState(playingState);
        Application.getAudioPlayer().play();
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
        if (currentTitleIndex < queue.size() - 1) {
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
