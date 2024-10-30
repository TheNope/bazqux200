package com.thenope.bazqux200.condense;

import com.thenope.bazqux200.config.classes.LibraryConfig;
import com.thenope.bazqux200.music.Playlist;
import com.thenope.bazqux200.util.DirectorySearch;
import com.thenope.bazqux200.util.ObservablePlaylists;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Condenser extends Task<Void> {
    private final Path libraryLocation;
    private final Path condensedLibraryLocation;
    private final DoubleProperty copyProgress;
    private final IntegerProperty countCopied;
    private final IntegerProperty countExisting;
    private final IntegerProperty countNotFound;
    private final IntegerProperty countRemoved;

    public Condenser(LibraryConfig libraryConfig) {
        libraryLocation = libraryConfig.getLocation();
        condensedLibraryLocation = libraryConfig.getCondensedLocation();
        copyProgress = new SimpleDoubleProperty(0);
        countCopied = new SimpleIntegerProperty(0);
        countExisting = new SimpleIntegerProperty(0);
        countNotFound = new SimpleIntegerProperty(0);
        countRemoved = new SimpleIntegerProperty(0);
    }

    public DoubleProperty copyProgressProperty() {
        return copyProgress;
    }

    public IntegerProperty countCopiedProperty() {
        return countCopied;
    }

    public IntegerProperty countExistingProperty() {
        return countExisting;
    }

    public IntegerProperty countNotFoundProperty() {
        return countNotFound;
    }

    public IntegerProperty countRemovedProperty() {
        return countRemoved;
    }

    private void updateProgress(int processedTitles, int numTitles) {
        copyProgress.set((double) processedTitles / numTitles);
    }

    public void copyTitles(ArrayList<Playlist> playlists) {
        ArrayList<Path> allTitles = new ArrayList<>(0);
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                allTitles.addAll(playlists.get(i).getContentFilePaths(false));
            } catch(IOException e) {
                // Copying failed
                e.printStackTrace();
                return;
            }
        }
        if (Files.notExists(condensedLibraryLocation)) {
            try {
                Files.createDirectories(condensedLibraryLocation);
            } catch(IOException e) {
                // Copying failed
                e.printStackTrace();
                return;
            }
        }
        int processedTitles = 0;
        int numTitles = allTitles.toArray().length;
        for (int i = 0; i < numTitles; i++) {
            int finalProcessedTitles = ++processedTitles;
            Path titlePath = allTitles.get(i);
            Path condensedTitlePath = Path.of(titlePath.toString().replace(libraryLocation.toString(), condensedLibraryLocation.toString()));
            if (Files.exists(condensedTitlePath)) {
                Platform.runLater(() -> {
                    countExisting.set(countExisting.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
                continue;
            }
            if (Files.notExists(titlePath)) {
                System.out.println(titlePath);
                Platform.runLater(() -> {
                    countNotFound.set(countNotFound.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
                continue;
            }
            if (Files.notExists(condensedTitlePath.getParent())) {
                try {
                    Files.createDirectories(condensedTitlePath.getParent());
                } catch (IOException e) {
                    // Copying failed
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                Platform.runLater(() -> {
                    countCopied.set(countCopied.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
                Files.copy(titlePath, condensedTitlePath);
            } catch (IOException e) {
                // Copying failed
                e.printStackTrace();
            }
        }
    }

    public void removeTitles(ArrayList<Playlist> playlists) {
        ArrayList<Path> currentTitles = DirectorySearch.findTitles(condensedLibraryLocation);
        ArrayList<Path> allTitles = new ArrayList<>(0);
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                allTitles.addAll(playlists.get(i).getContentFilePaths(true));
            } catch (IOException e) {
                // Removing failed
                e.printStackTrace();
                return;
            }
        }
        for (int i = 0; i < currentTitles.toArray().length; i++) {
            Path currentTitle = currentTitles.get(i);
            if (!allTitles.contains(Path.of(currentTitle.toString().replace(condensedLibraryLocation.toString(), libraryLocation.toString())))) {
                try {
                    Platform.runLater(() -> countRemoved.set(countRemoved.get() + 1));
                    Files.delete(currentTitle);
                } catch (IOException e) {
                    // Removing failed
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyPlaylists(ArrayList<Playlist> playlists) {
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                Files.copy(
                        playlists.get(i).getPath(),
                        Path.of(playlists.get(i).getPath().toString().replace(libraryLocation.toString(), condensedLibraryLocation.toString())),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                // Copying failed
                e.printStackTrace();
                return;
            }
        }
    }

    public void condense() {
        ArrayList<Playlist> playlists = ObservablePlaylists.getPlaylists();
        copyTitles(playlists);
        removeTitles(playlists);
        copyPlaylists(playlists);
    }

    @Override
    protected Void call() {
        condense();
        return null;
    }
}
