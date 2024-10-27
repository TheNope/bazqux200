package com.thenope.bazqux200;

import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;
import com.thenope.bazqux200.music.Title;
import com.thenope.bazqux200.music.Playlist;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.thenope.bazqux200.util.ObservablePlaylists.getObservablePlaylists;
import static com.thenope.bazqux200.util.ObservableTitles.getObservableTitles;

public class MainViewController {
    private AtomicBoolean updatingProgressSlider;

    @FXML
    private Button modeButton;

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
    private Label progressLabel;

    @FXML
    private Slider progressSlider;

    @FXML
    private Slider volumeSlider;

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
    protected void onModeButtonClick() {
        Application.getCurrentPlaybackQueue().nextPlayingMode();
        modeButton.setText(Application.getCurrentPlaybackQueue().getPlayingMode());
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
        trackColumn.setCellValueFactory(cellData -> cellData.getValue().trackProperty());
        albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());

        titleTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        titleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println(newValue.getName());
            }
        });
    }

    @FXML
    protected void initProgressSlider() {
        updatingProgressSlider = new AtomicBoolean(false);

        progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(!updatingProgressSlider.get()) {
                Application.getCurrentPlaybackQueue().setProgress(newValue);
            }
        });
    }

    @FXML
    protected void initVolumeSlider() {
        int initialVolume = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getPlayerConfig().getInitialVolume();
        volumeSlider.setValue(initialVolume);
        Application.getAudioPlayer().setVolume(initialVolume);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Application.getAudioPlayer().setVolume(newValue.intValue());
        });
    }

    @FXML
    public void initialize() {
        initPlaylistView();
        initTitleView();
        initProgressSlider();
        initVolumeSlider();

        Thread updateThread = new Thread(() -> {
            while (true) {
                if(Application.getCurrentPlaybackQueue().isPlaying()) {
                    float position = (float) Application.getAudioPlayer().getTime() / Application.getAudioPlayer().getLength() * 100;
                    String formattedProgress =
                            Application.getAudioPlayer().formattedTime()
                            + " / "
                            + Application.getCurrentPlaybackQueue().getCurrentTitle().durationProperty().getValue();

                    Platform.runLater(() -> {
                        updatingProgressSlider.set(true);
                        progressSlider.setValue(position);
                        progressLabel.setText(formattedProgress);
                        updatingProgressSlider.set(false);

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
}