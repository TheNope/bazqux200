package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;



public class AudioPlayer {
    private AudioPlayerComponent audioPlayer;

    public AudioPlayer() {
        audioPlayer = new AudioPlayerComponent();
        audioPlayer.mediaPlayer().audio().setVolume(50);
    }

    public void setTitle(Title title) {
        audioPlayer.mediaPlayer().controls().stop();
        System.out.println(getLength());
        audioPlayer.mediaPlayer().media().startPaused(title.getPath().toString());
    }

    public void play() {
        audioPlayer.mediaPlayer().controls().start();
    }

    public void proceed() {
        audioPlayer.mediaPlayer().controls().play();
    }

    public void pause() {
        audioPlayer.mediaPlayer().controls().pause();
    }

    public long getTime() {
        return audioPlayer.mediaPlayer().status().time();
    }

    public long getLength() {
        return audioPlayer.mediaPlayer().status().length();
    }

    public void skipTo(long time) {
        audioPlayer.mediaPlayer().controls().setTime(time);
    }
}
