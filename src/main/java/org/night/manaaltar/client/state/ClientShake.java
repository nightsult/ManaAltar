package org.night.manaaltar.client.state;

public final class ClientShake {
    private ClientShake() {}

    public static long startTick = 0;
    public static long endTick   = 0;
    public static float baseIntensity = 0f;

    public static void start(long gameTime, int durationTicks, float intensity) {
        startTick = gameTime;
        endTick   = gameTime + Math.max(1, durationTicks);
        baseIntensity = Math.max(0f, intensity);
    }

    public static boolean active(long gameTime) {
        return gameTime < endTick;
    }

    public static float factor(long gameTime, float partialTick) {
        float left = Math.max(0f, (endTick - gameTime - partialTick));
        float dur  = Math.max(1f, endTick - startTick);
        float t    = left / dur;
        return t * t;
    }
}
