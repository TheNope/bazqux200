package com.thenope.bazqux200;

import com.thenope.bazqux200.music.PlaybackQueue;
import com.thenope.bazqux200.music.Title;
import com.thenope.bazqux200.music.Playlist;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    protected void onPlayButtonClick() {
        if(Application.currentPlaybackQueue.getQueue() != Application.potentialPlaybackQueue.getQueue()) {
            Application.currentPlaybackQueue.setQueue(Application.potentialPlaybackQueue.getQueue());
        }
        if(Application.currentPlaybackQueue.isReady()) {
            Application.currentPlaybackQueue.play();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPauseButtonClick() {
        if(Application.currentPlaybackQueue.isPlaying()) {
            Application.currentPlaybackQueue.pause();
        } else if (!Application.currentPlaybackQueue.isPlaying() && Application.currentPlaybackQueue.isReady()) {
            Application.currentPlaybackQueue.play();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onPreviousButtonClick() {
        if(Application.currentPlaybackQueue.isReady()) {
            Application.currentPlaybackQueue.previous();
        }
        titleTableView.refresh();
    }

    @FXML
    protected void onNextButtonClick() {
        if(Application.currentPlaybackQueue.isReady()) {
            Application.currentPlaybackQueue.next();
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
                Application.potentialPlaybackQueue.setQueue(newValue.getContent());
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
    public void initialize() {
        initPlaylistView();
        initTitleView();
    }
}