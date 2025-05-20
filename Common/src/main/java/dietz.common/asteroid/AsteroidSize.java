package dietz.common.asteroid;

import java.util.concurrent.ThreadLocalRandom;
import java.util.prefs.BackingStoreException;

public enum AsteroidSize {
    GIANT (90f, 150f, true, 3, 4),
    LARGE ( 60f, 85f, true, 2, 3),
    MEDIUM( 40f,55f, true, 1, 2),
    SMALL ( 20f,  35f, false,0, 0);

    private static final float BASE_SPEED = 500f;

    private final float minRadius;
    private final float maxRadius;
    private final boolean canSplit;
    private final int minFragments;
    private final int maxFragments;

    AsteroidSize(float minRadius, float maxRadius, boolean canSplit, int minFragments, int maxFragments) {
        this.minRadius   = minRadius;
        this.maxRadius   = maxRadius;
        this.canSplit    = canSplit;
        this.minFragments= minFragments;
        this.maxFragments= maxFragments;
    }
    public float getRadius() {
        if (minRadius == maxRadius) return minRadius;

        return ThreadLocalRandom.current().nextFloat() * (maxRadius - minRadius) + minRadius;
    }

    public boolean canSplit() {
        return canSplit;
    }

    public AsteroidSize nextSize() {
        return switch (this) {
            case GIANT  -> LARGE;
            case LARGE  -> MEDIUM;
            case MEDIUM -> SMALL;
            case SMALL  -> null;
        };
    }

    public float getSpeed() {
        return BASE_SPEED / getRadius();
    }
    public int getRandomFragments() {
        if (maxFragments <= minFragments) return minFragments;
        return minFragments + (int)(Math.random() * (maxFragments - minFragments + 1));
    }
}
