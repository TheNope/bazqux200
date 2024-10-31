package com.thenope.bazqux200;

import com.thenope.bazqux200.music.AudioPlayer;
import com.thenope.bazqux200.music.PlaybackQueue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Application extends javafx.application.Application {
    private static AudioPlayer audioPlayer;
    private static PlaybackQueue currentPlaybackQueue;
    private static PlaybackQueue potentialPlaybackQueue;
    private static Logger LOGGER;

    public static AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public static PlaybackQueue getCurrentPlaybackQueue() {
        return currentPlaybackQueue;
    }

    public static PlaybackQueue getPotentialPlaybackQueue() {
        return potentialPlaybackQueue;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("css/default.css")).toString());
        stage.setTitle("bazqux200");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        audioPlayer = new AudioPlayer();
        currentPlaybackQueue = new PlaybackQueue();
        potentialPlaybackQueue = new PlaybackQueue();
        LOGGER = LogManager.getLogger();
        LOGGER.debug("Debug-Nachricht");
        LOGGER.info("Info-Nachricht");
        LOGGER.warn("Warn-Nachricht");
        LOGGER.error("Error-Nachricht");
        launch();
    }
}