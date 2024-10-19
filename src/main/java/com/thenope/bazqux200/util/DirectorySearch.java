package com.thenope.bazqux200.util;

import com.thenope.bazqux200.music.Playlist;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DirectorySearch {
    public static ArrayList<Path> findFiles(Path path) {
        ArrayList<Path> files = new ArrayList<Path>(0);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
                    files.add(filePath.toAbsolutePath());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path filePath, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static ArrayList<Path> findPlaylists(Path path) {
        ArrayList<Path> files = findFiles(path);
        ArrayList<Path> playlists = new ArrayList<Path>(0);
        for(int i = 0; i < files.toArray().length; i++) {
            Path filePath = (Path) files.toArray()[i];
            if(filePath.toString().endsWith("m3u")) {
                playlists.add(filePath);
            }
        }
        return playlists;
    }
}
