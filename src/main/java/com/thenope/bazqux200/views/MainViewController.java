package com.thenope.bazqux200.views;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;
import com.thenope.bazqux200.music.Title;
import com.thenope.bazqux200.music.Playlist;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
    private BorderPane artworkBorderPane;

    @FXML
    private ImageView artworkImageView;

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
    protected void onCondenseMenuItemClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("views/condense-view.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getPlayerConfig().getTheme().toString());

            Stage condenseStage = new Stage();
            condenseStage.setTitle("Condense");
            condenseStage.setScene(scene);
            condenseStage.setResizable(false);
            condenseStage.show();
        } catch (IOException e) {
            Application.getLogger().error(e.getMessage());
        }
    }

    @FXML
    protected void onTitleContextMenuPlayClick() {
        Title selectedTitle = titleTableView.getSelectionModel().selectedItemProperty().get();
        if (selectedTitle == null) return;
        if (Application.getCurrentPlaybackQueue().getQueue() != Application.getPotentialPlaybackQueue().getQueue()) {
            Application.getCurrentPlaybackQueue().setQueue(Application.getPotentialPlaybackQueue());
        }
        Application.getCurrentPlaybackQueue().setCurrentTitle(selectedTitle);
        Application.getCurrentPlaybackQueue().play();
        titleTableView.refresh();
    }

    @FXML
    protected void onPlayButtonClick() {
        if (Application.getCurrentPlaybackQueue().getQueue() != Application.getPotentialPlaybackQueue().getQueue()) {
            Application.getCurrentPlaybackQueue().setQueue(Application.getPotentialPlaybackQueue());
        }
        if (Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().play();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPauseButtonClick() {
        if (Application.getCurrentPlaybackQueue().isPlaying()) {
            Application.getCurrentPlaybackQueue().pause();
        } else if (!Application.getCurrentPlaybackQueue().isPlaying() && Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().proceed();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPreviousButtonClick() {
        if (Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().previous();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onNextButtonClick() {
        if (Application.getCurrentPlaybackQueue().isReady()) {
            Application.getCurrentPlaybackQueue().next();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onModeButtonClick() {
        Application.getCurrentPlaybackQueue().nextPlayingMode();
        Application.getPotentialPlaybackQueue().nextPlayingMode();
        modeButton.setText(Application.getCurrentPlaybackQueue().getStringPlayingMode());
    }

    @FXML
    public void initPlaylistView() {
        //playlistNameColumn.prefWidthProperty().bind(playlistTableView.widthProperty());
        playlistNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        playlistTableView.setItems(getObservablePlaylists(Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getLibraryConfig().getLocation()));
        playlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        playlistTableView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                ArrayList<Title> playlistContent = new ArrayList<>(0);
                try {
                    playlistContent = newValue.getContent();
                } catch (IOException e) {
                    Application.getLogger().error(e.getMessage());
                }
                Application.getPotentialPlaybackQueue().setQueue(playlistContent);
                titleTableView.setItems(getObservableTitles(playlistContent));
            }
        });
    }

    @FXML
    public void initArtworkImageView() {
        artworkBorderPane.widthProperty().addListener((_, _, newVal) -> artworkImageView.setFitWidth(newVal.doubleValue()));
        artworkBorderPane.heightProperty().addListener((_, _, newVal) -> artworkImageView.setFitHeight(newVal.doubleValue()));
    }

    @FXML
    public void initTitleView() {
        playingColumn.setCellValueFactory(cellData -> cellData.getValue().playingStateProperty().asString());
        titleNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        trackColumn.setCellValueFactory(cellData -> cellData.getValue().trackProperty());
        albumColumn.setCellValueFactory(cellData -> cellData.getValue().albumProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty());
        titleTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    protected void initProgressSlider() {
        updatingProgressSlider = new AtomicBoolean(false);

        progressSlider.valueProperty().addListener((_, _, newValue) -> {
            if (!updatingProgressSlider.get()) {
                Application.getCurrentPlaybackQueue().setProgress(newValue);
            }
        });
    }

    @FXML
    protected void initVolumeSlider() {
        int initialVolume = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getPlayerConfig().getInitialVolume();
        volumeSlider.setValue(initialVolume);
        Application.getAudioPlayer().setVolume(initialVolume);

        volumeSlider.valueProperty().addListener((_, _, newValue) -> Application.getAudioPlayer().setVolume(newValue.intValue()));
    }

    @FXML
    public void initialize() {
        initPlaylistView();
        initArtworkImageView();
        initTitleView();
        initProgressSlider();
        initVolumeSlider();

        Thread updateThread = new Thread(() -> {
            while (true) {
                if (Application.getCurrentPlaybackQueue().isPlaying()) {
                    Title currentTitle = Application.getCurrentPlaybackQueue().getCurrentTitle();
                    float position = (float) Application.getAudioPlayer().getTime() / Application.getAudioPlayer().getLength() * 100;
                    String formattedProgress =
                            Application.getAudioPlayer().formattedTime()
                            + " / "
                            + currentTitle.durationProperty().getValue();

                    Platform.runLater(() -> {
                        Application.setTitle("bazqux200 - " + "[" + currentTitle.albumProperty().getValue() + "] " + currentTitle.getName());
                        updatingProgressSlider.set(true);
                        progressSlider.setValue(position);
                        progressLabel.setText(formattedProgress);
                        updatingProgressSlider.set(false);
                        artworkImageView.setImage(currentTitle.imageProperty());

                        if(position == 100) {
                            Application.getCurrentPlaybackQueue().next();
                            titleTableView.refresh();
                        }
                    });
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Application.getLogger().error(e.getMessage());
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }
}