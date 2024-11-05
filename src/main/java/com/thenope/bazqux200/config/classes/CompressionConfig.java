package com.thenope.bazqux200.config.classes;

import java.nio.file.Path;

public class CompressionConfig {
    private boolean enabled;
    private int bitrate;

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getBitrate() { return bitrate; }

    public void setBitrate(int bitrate) { this.bitrate = bitrate; }
}
