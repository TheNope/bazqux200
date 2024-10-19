package com.thenope.bazqux200.util;

public enum PlayingState {
    INACTIVE,
    PAUSED,
    PLAYING;

    @Override
    public String toString() {
        return switch (this) {
            case PAUSED -> "⏸";
            case PLAYING -> "⏵";
            default -> "";
        };
    }
};
