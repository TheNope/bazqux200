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
    private Path path;
    private ArrayList<Title> content;

    public Playlist(Path libraryLocation, Path playlistPath) {
        path = libraryLocation.resolve(playlistPath);
        content = new ArrayList<>(0);
    }

    public ArrayList<Title> getContent() throws IOException {
        if(content.toArray().length == 0) {
            Path libraryLocation = Objects.requireNonNull(ConfigLoader.getConfig(AppConfig.class)).getLibraryConfig().getLocation();
            BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
            String titlePathString;
            while((titlePathString = reader.readLine()) != null) {
                Path titlePath = libraryLocation.resolve(Path.of(titlePathString));
                content.add(new Title(titlePath));
            }
            reader.close();
        }
        return content;
    }

    public String getName() {
        String[] splitPath = path.toString().split("\\\\");
        return splitPath[splitPath.length - 1].replace(".m3u", "");
    }
}
