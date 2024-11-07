package com.thenope.bazqux200.condense;

import com.thenope.bazqux200.Application;
import com.thenope.bazqux200.music.Metadata;
import ws.schild.jave.*;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Path;

public class Compressor {
    public static void compress(Path source, Path target, int bitrate) throws EncoderException {
        File sourceFile = new File(source.toString());
        File targetFile = new File(target.toString());

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(bitrate * 1000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setAudioAttributes(audio);

        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(sourceFile), targetFile, attrs);

        try {
            Metadata.copyArtwork(sourceFile, targetFile);
        }
        catch (Exception e) {
            Application.getLogger().error(e.getMessage());
        }
    }
}
