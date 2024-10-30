package com.thenope.bazqux200.music;

import com.thenope.bazqux200.config.ConfigLoader;
import com.thenope.bazqux200.config.classes.AppConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class Playlist {
    private final Path path;
    private final ArrayList<Title> content;

    public Playlist(Path libraryLocation, Path playlistPath) {
        path = libraryLocation.resolve(playlistPath);
        content = new ArrayList<>(0);
    }

    public Path getPath() {
        return path;
    }

    public ArrayList<Title> getContent() throws IOException {
        if (content.toArray().length == 0) {
            ArrayList<Path> contentFilePaths = getContentFilePaths(false);
            for (int i = 0; i < contentFilePaths.toArray().length; i++) {
                content.add(new Title(contentFilePaths.get(i)));
            }
        }
        return content;
    }

    public ArrayList<Path> getContentFilePaths(Boolean absolute) throws IOException {
        Path libraryLocation = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getLibraryConfig().getLocation();
        BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
        ArrayList<Path> contentFilePaths = new ArrayList<>(0);
        String titlePathString;
        while ((titlePathString = reader.readLine()) != null) {
            Path titlePath = libraryLocation.resolve(Path.of(titlePathString));
            if (absolute) {
                titlePath = titlePath.toAbsolutePath();
            }
            contentFilePaths.add(titlePath);
        }
        return contentFilePaths;
    }

    public String getName() {
        String[] splitPath = path.toString().split("\\\\");
        return splitPath[splitPath.length - 1].replace(".m3u", "");
    }
}
