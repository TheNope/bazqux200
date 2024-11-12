package com.thenope.bazqux200.util;

public class Random {
    public static int RandomPositiveInt(int avoid, int max) {
        if (max <= 0) return 0;
        java.util.Random random = new java.util.Random();
        int randomPositveInt;
        do {
            randomPositveInt = random.nextInt(max);
        } while (randomPositveInt == avoid);
        return randomPositveInt;
    }
}
