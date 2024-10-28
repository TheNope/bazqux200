package com.thenope.bazqux200.util;

import com.thenope.bazqux200.music.Title;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ObservableTitles {
    public static ObservableList<Title> getObservableTitles(ArrayList<Title> titles) {
        return FXCollections.observableArrayList(titles);
    }
}
