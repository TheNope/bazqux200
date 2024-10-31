package com.thenope.bazqux200.util;

import com.thenope.bazqux200.Application;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class DirectorySearch {
    public static ArrayList<Path> findFiles(Path path, String[] extensions) {
        ArrayList<Path> files = new ArrayList<>(0);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
                    for (String extension : extensions) {
                        if (filePath.toString().endsWith(extension)) {
                            files.add(filePath.toAbsolutePath());
                            break;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path filePath, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            Application.getLogger().error(e.getMessage());
        }
        return files;
    }

    public static ArrayList<Path> findPlaylists(Path path) {
        return findFiles(path, new String[]{"m3u"});
    }

    public static ArrayList<Path> findTitles(Path path) {
        return findFiles(path, new String[]{"mp3", "flac"});
    }
}
