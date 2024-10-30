package com.thenope.bazqux200.music;

import com.thenope.bazqux200.Application;
import uk.co.caprica.vlcj.player.component.AudioPlayerComponent;



public class AudioPlayer {
    private final AudioPlayerComponent audioPlayer;

    public AudioPlayer() {
        audioPlayer = new AudioPlayerComponent();
        audioPlayer.mediaPlayer().audio().setVolume(50);
    }

    public void setTitle(Title title) {
        audioPlayer.mediaPlayer().controls().stop();
        audioPlayer.mediaPlayer().media().startPaused(title.getPath().toString());
    }

    public void setVolume(Integer volume) {
        audioPlayer.mediaPlayer().audio().setVolume(volume);
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

    public void skipTo(long time) {
        audioPlayer.mediaPlayer().controls().setTime(time);
    }

    public long getTime() {
        return audioPlayer.mediaPlayer().status().time();
    }

    public long getLength() {
        return audioPlayer.mediaPlayer().status().length();
    }

    public String formattedTime() {
        long time = Application.getAudioPlayer().getTime();
        String currentSeconds = String.format("%02d", time / 1000 % 60);
        String currentMinutes = String.valueOf(time / 1000 / 60);
        return currentMinutes + ":" + currentSeconds;
    }

    public String formattedLength() {
        long length = Application.getAudioPlayer().getLength();
        String fullSeconds = String.format("%02d", length / 1000 % 60);
        String fullMinutes = String.valueOf(length / 1000 / 60);
        return fullMinutes + ":" + fullSeconds;
    }
}
