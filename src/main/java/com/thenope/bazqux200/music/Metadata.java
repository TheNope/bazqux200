package com.thenope.bazqux200.music;

import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Metadata {
    private final AudioFile title;
    private final Tag titleTags;

    public Metadata(Path titlePath) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException {
        title = AudioFileIO.read(new File(titlePath.toString()));
        titleTags = title.getTag();
    }

    public String getTitle() {
        return titleTags.getFirst(FieldKey.TITLE);
    }

    public String getTrack() {
        return titleTags.getFirst(FieldKey.TRACK);
    }

    public String getAlbum() {
        return titleTags.getFirst(FieldKey.ALBUM);
    }

    public long getBitrate() {
        return title.getAudioHeader().getBitRateAsNumber();
    }

    public Image getArtwork() {
        Artwork artwork = titleTags.getFirstArtwork();
        if (artwork == null) return null;
        return new Image(new ByteArrayInputStream(artwork.getBinaryData()));
    }

    public String getFormattedDuration() {
        int duration = title.getAudioHeader().getTrackLength();
        String durationSeconds = String.format("%02d", duration  % 60);
        String durationMinutes = String.valueOf(duration / 60);
        return durationMinutes + ":" + durationSeconds;
    }

    public static void copyArtwork(File source, File target) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
        AudioFile sourceAudioFile = AudioFileIO.read(source);
        AudioFile targetAudioFile = AudioFileIO.read(target);
        Artwork artwork = sourceAudioFile.getTag().getFirstArtwork();
        targetAudioFile.getTag().setField(artwork);
        targetAudioFile.commit();
    }
}
