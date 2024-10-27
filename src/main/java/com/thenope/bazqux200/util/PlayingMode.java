package com.thenope.bazqux200.util;

public enum PlayingMode {
    REPEAT,
    SHUFFLE,
    RANDOM;

    @Override
    public String toString() {
        return switch (this) {
            case REPEAT -> "\uD83D\uDD01";
            case SHUFFLE -> "\uD83D\uDD00";
            case RANDOM -> "?";
        };
    }
}
