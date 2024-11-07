package com.thenope.bazqux200.condense;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.config.classes.CondenseConfig;
import com.thenope.bazqux200.music.Metadata;
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
    private final CondenseConfig condenseConfig;
    private final Path libraryLocation;
    private final DoubleProperty copyProgress;
    private final IntegerProperty countCopied;
    private final IntegerProperty countExisting;
    private final IntegerProperty countNotFound;
    private final IntegerProperty countRemoved;

    public Condenser(Path libraryLocation, CondenseConfig condenseConfig) {
        this.condenseConfig = condenseConfig;
        this.libraryLocation = libraryLocation;
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

    private boolean compressCondition(Path titlePath) {
        if (!condenseConfig.getCompressionConfig().getEnabled()) return false;
        else if (titlePath.toString().endsWith("flac")) return true;
        else {
            try{
                if (new Metadata(titlePath).getBitrate() > condenseConfig.getCompressionConfig().getBitrate()) return true;
            } catch (Exception e) {
                Application.getLogger().error(e.getMessage());
                return true;
            }
        }
        return false;
    }

    public void copyTitles(ArrayList<Playlist> playlists) {
        ArrayList<Path> allTitles = new ArrayList<>(0);
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                allTitles.addAll(playlists.get(i).getContentFilePaths(false));
            } catch(IOException e) {
                // Copying failed
                Application.getLogger().error(e.getMessage());
                return;
            }
        }
        if (Files.notExists(condenseConfig.getLocation())) {
            try {
                Files.createDirectories(condenseConfig.getLocation());
            } catch(IOException e) {
                // Copying failed
                Application.getLogger().error(e.getMessage());
                return;
            }
        }
        int processedTitles = 0;
        int numTitles = allTitles.toArray().length;
        for (int i = 0; i < numTitles; i++) {
            int finalProcessedTitles = ++processedTitles;
            Path titlePath = allTitles.get(i);
            Path condensedTitlePath = Path.of(titlePath.toString().replace(libraryLocation.toString(), condenseConfig.getLocation().toString()));
            Path compressedTitlePath = Path.of(condensedTitlePath.toString().replace("flac", "mp3"));

            // Check if title already exists in condensed location
            if (Files.exists(condensedTitlePath) || (Files.exists(compressedTitlePath) && condenseConfig.getCompressionConfig().getEnabled())) {
                Application.getLogger().info("File already exists: {}", titlePath);
                Platform.runLater(() -> {
                    countExisting.set(countExisting.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
                continue;
            }

            // Check if file exists in library
            if (Files.notExists(titlePath)) {
                Application.getLogger().error("File not found: {}", titlePath);
                Platform.runLater(() -> {
                    countNotFound.set(countNotFound.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
                continue;
            }

            // Create directory if it doesn't exist
            if (Files.notExists(condensedTitlePath.getParent())) {
                try {
                    Files.createDirectories(condensedTitlePath.getParent());
                } catch (IOException e) {
                    // Copying failed
                    Application.getLogger().error(e.getMessage());
                    continue;
                }
            }

            // Copy or compress and copy
            try {
                if (compressCondition(titlePath)) {
                    Compressor.compress(titlePath, compressedTitlePath, condenseConfig.getCompressionConfig().getBitrate());
                    Application.getLogger().info("File compressed and copied: {}", titlePath);
                } else {
                    Files.copy(titlePath, condensedTitlePath);
                    Application.getLogger().info("File copied: {}", titlePath);
                }
                Platform.runLater(() -> {
                    countCopied.set(countCopied.get() + 1);
                    updateProgress(finalProcessedTitles, numTitles);
                });
            } catch (Exception e) {
                // Copying failed
                Application.getLogger().error(e.getMessage());
            }
        }
    }

    public void removeTitles(ArrayList<Playlist> playlists) {
        ArrayList<Path> currentTitles = DirectorySearch.findTitles(condenseConfig.getLocation());
        ArrayList<Path> allTitles = new ArrayList<>(0);
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                allTitles.addAll(playlists.get(i).getContentFilePaths(true));
            } catch (IOException e) {
                // Removing failed
                Application.getLogger().error(e.getMessage());
                return;
            }
        }
        for (int i = 0; i < currentTitles.toArray().length; i++) {
            Path currentTitle = currentTitles.get(i);
            if (!allTitles.contains(Path.of(currentTitle.toString().replace(condenseConfig.getLocation().toString(), libraryLocation.toString())))) {
                try {
                    Files.delete(currentTitle);
                    Platform.runLater(() -> countRemoved.set(countRemoved.get() + 1));
                    Application.getLogger().info("File removed: {}", currentTitle);
                } catch (IOException e) {
                    // Removing failed
                    Application.getLogger().error(e.getMessage());
                }
            }
        }
    }

    public void copyPlaylists(ArrayList<Playlist> playlists) {
        for (int i = 0; i < playlists.toArray().length; i++) {
            try {
                Path source = playlists.get(i).getPath();
                Path destination = Path.of(playlists.get(i).getPath().toString().replace(libraryLocation.toString(), condenseConfig.getLocation().toString()));
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                if (condenseConfig.getCompressionConfig().getEnabled()) {
                    String playlistContent = new String(Files.readAllBytes(source));
                    playlistContent = playlistContent.replace(".flac", ".mp3");
                    Files.write(destination, playlistContent.getBytes());
                }
            } catch (IOException e) {
                // Copying failed
                Application.getLogger().error(e.getMessage());
                return;
            }
        }
    }

    public void condense() {
        ArrayList<Playlist> playlists = ObservablePlaylists.getPlaylists(libraryLocation);
        copyTitles(playlists);
        copyPlaylists(playlists);
        playlists = ObservablePlaylists.getPlaylists(condenseConfig.getLocation());
        removeTitles(playlists);
    }

    @Override
    protected Void call() {
        condense();
        return null;
    }
}
