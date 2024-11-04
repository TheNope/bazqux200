package com.thenope.bazqux200.condense;

import ws.schild.jave.*;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.nio.file.Path;

public class Compressor {
    public static void compress(Path source, Path destination) throws EncoderException {
        File sourceFile = new File(source.toString());
        File destinationFile = new File(destination.toString());

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(320000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        EncodingAttributes attrs = new EncodingAttributes();
        //attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(sourceFile), destinationFile, attrs);
    }
}
