package com.thenope.bazqux200;

import com.thenope.bazqux200.music.Title;
import com.thenope.bazqux200.music.Playlist;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.thenope.bazqux200.util.ObservablePlaylists.getObservablePlaylists;
import static com.thenope.bazqux200.util.ObservableTitles.getObservableTitles;

public class MainViewController {
    @FXML
    private TableView<Playlist> playlistTableView;

    @FXML
    private TableColumn<Playlist, String> playlistNameColumn;

    @FXML
    private TableView<Title> titleTableView;

    @FXML
    private TableColumn<Title, String> playingColumn;

    @FXML
    private TableColumn<Title, String> titleNameColumn;

    @FXML
    private TableColumn<Title, String> trackColumn;

    @FXML
    private TableColumn<Title, String> albumColumn;

    @FXML
    private TableColumn<Title, String> durationColumn;

    @FXML
    private Slider progressSlider;

    @FXML
    protected void onPlayButtonClick() {
        if(Application.getCurrentPlaybackQueue().getQueue() != Application.getPotentialPlaybackQueue().getQueue()) {
            Application.getCurrentPlaybackQueue().setQueue(Application.getPotentialPlaybackQueue().getQueue());
        }
        if(Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().play();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPauseButtonClick() {
        if(Application.getCurrentPlaybackQueue().isPlaying()) {
            Application.getCurrentPlaybackQueue().pause();
        } else if (!Application.getCurrentPlaybackQueue().isPlaying() && Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().proceed();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPreviousButtonClick() {
        if(Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().previous();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onNextButtonClick() {
        if(Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().next();
        }
        titleTableView.refresh();
    }

    @FXML
    public void initPlaylistView() {
        playlistNameColumn.prefWidthProperty().bind(playlistTableView.widthProperty());
        playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        playlistTableView.setItems(getObservablePlaylists());
        playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        playlistTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Application.getPotentialPlaybackQueue().setQueue(newValue.getContent());
                titleTableView.setItems(getObservableTitles(newValue));
            }
        });
    }

    @FXML
    public void initTitleView() {
        playingColumn.setCellValueFactory(cellData -> cellData.getValue().playingStateProperty().asString());
        titleNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        titleTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        titleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.getName());
            }
        });
    }

    @FXML
    protected void initProgressSlider() {
        AtomicBoolean updatingSlider = new AtomicBoolean(false);

        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!updatingSlider.get()) {
                Application.getCurrentPlaybackQueue().setProgress(newValue);
            }
        });

        Thread updateThread = new Thread(() -> {
            while (true) {
                if(Application.getCurrentPlaybackQueue().isPlaying()) {
                    float position = (float) Application.getAudioPlayer().getTime() / Application.getAudioPlayer().getLength() * 100;

                    Platform.runLater(() -> {
                        updatingSlider.set(true);
                        progressSlider.setValue(position);
                        updatingSlider.set(false);

                        if(position == 100) {
                            Application.getCurrentPlaybackQueue().next();
                            titleTableView.refresh();
                        }
                    });
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        updateThread.setDaemon(true);
        updateThread.start();
    }

    @FXML
    public void initialize() {
        initPlaylistView();
        initTitleView();
        initProgressSlider();
    }
}