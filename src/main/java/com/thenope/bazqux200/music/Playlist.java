package com.thenope.bazqux200.music;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Playlist {
    private Path path;
    private ArrayList<Title> content;

    public Playlist(Path path) throws IOException {
        this.path = path;
        BufferedReader reader = new BufferedReader(new FileReader(path.toString()));
        content = new ArrayList<Title>(0);
        String titlePathString;
        while((titlePathString = reader.readLine()) != null) {
            Path titlePath = Path.of(titlePathString);
            content.add(new Title(titlePath));
        }
        reader.close();
    }

    public ArrayList<Title> getContent() {
        return content;
    }

    public String getName() {
        String[] splittedPath = path.toString().split("\\\\");
        return splittedPath[splittedPath.length - 1];
    }
}
