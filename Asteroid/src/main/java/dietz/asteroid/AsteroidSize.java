package dietz.asteroid;

import java.util.concurrent.ThreadLocalRandom;

public enum AsteroidSize {
    GIANT (120f, 135f, true, 2, 3),
    LARGE ( 90f, 105f, true, 2, 3),
    MEDIUM( 60f,75f, true, 1, 3),
    SMALL ( 30f,  45f, false,0, 0);

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
