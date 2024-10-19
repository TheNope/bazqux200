package com.thenope.bazqux200;

import com.thenope.bazqux200.music.Title;
import com.thenope.bazqux200.music.Playlist;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import static com.thenope.bazqux200.util.ObservablePlaylists.getObservablePlaylists;
import static com.thenope.bazqux200.util.ObservableTitles.getObservableTitles;

public class MainViewController {
    @FXML
    private TableView<Playlist> playlistTableView;

    @FXML
    private TableColumn<String, String> playlistNameColumn;

    @FXML
    private TableView<Title> titleTableView;

    @FXML
    protected void onPlayButtonClick() {
        if(Application.playbackQueue.isReady()) {
            Application.playbackQueue.play();
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        if(Application.playbackQueue.isPlaying()) {
            Application.playbackQueue.pause();
        } else if (!Application.playbackQueue.isPlaying() && Application.playbackQueue.isReady()) {
            Application.playbackQueue.play();
        }
    }

    @FXML
    protected void onPreviousButtonClick() {
        if(Application.playbackQueue.isReady()) {
            Application.playbackQueue.previous();
        }
    }

    @FXML
    protected void onNextButtonClick() {
        if(Application.playbackQueue.isReady()) {
            Application.playbackQueue.next();
        }
    }

    @FXML
    public void initPlaylistView() {
        playlistNameColumn.prefWidthProperty().bind(playlistTableView.widthProperty());
//        playlistListView.setItems(getObservablePlaylists());
//        playlistListView.setCellFactory(new Callback<ListView<Playlist>, ListCell<Playlist>>() {
//            @Override
//            public ListCell<Playlist> call(ListView<Playlist> listView) {
//                return new ListCell<Playlist>() {
//                    @Override
//                    protected void updateItem(Playlist playlist, boolean empty) {
//                        super.updateItem(playlist, empty);
//                        if (empty || playlist == null) {
//                            setText(null);
//                        } else {
//                            setText(playlist.getName());
//                        }
//                    }
//                };
//            }
//        });
//        playlistListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        playlistListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                Application.playbackQueue.setQueue(newValue.getContent());
//                titleListView.setItems(getObservableTitles(newValue));
//            }
//        });
    }

    @FXML
    public void initTitleView() {
//        titleListView.setCellFactory(new Callback<ListView<Title>, ListCell<Title>>() {
//            @Override
//            public ListCell<Title> call(ListView<Title> listView) {
//                return new ListCell<Title>() {
//                    @Override
//                    protected void updateItem(Title title, boolean empty) {
//                        super.updateItem(title, empty);
//                        if (empty || title == null) {
//                            setText(null);
//                        } else {
//                            setText(title.getName());
//                        }
//                    }
//                };
//            }
//        });
//        titleListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        titleListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                System.out.println(newValue);
//            }
//        });
    }

    @FXML
    public void initialize() {
        initPlaylistView();
        initTitleView();
    }
}