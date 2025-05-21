package dietz.asteroid;

import dietz.common.asteroid.AsteroidSize;
import dietz.common.data.Entity;
import javafx.scene.paint.Color;

import java.util.Random;

public class Asteroid extends Entity {
    private final AsteroidSize size;
    private final float        radius;   // the one true radius
    private final double       dx, dy;   // velocities based on that radius
    private static final Random random = new Random();

    public Asteroid(AsteroidSize size, double x, double y, double direction) {
        this.size = size;

        // 1) pick a single, slightly‐random radius (±20%)
        float base = size.getRadius();
        this.radius = base * (0.8f + random.nextFloat() * 0.4f);

        // 2) generate the fixed polygon once
        setPolygonCoordinates(generateShape(this.radius));

        // 3) tell the collision system the true radius
        setRadius(this.radius);

        setX(x);
        setY(y);

        // 4) compute speed inversely proportional to radius
        float speed = size.getSpeed();

        double rad = Math.toRadians(direction);
        this.dx = Math.cos(rad) * speed;
        this.dy = Math.sin(rad) * speed;
    }

    private double[] generateShape(float baseRadius) {
        final int vertices   = random.nextInt(8,24) ;
        double[] shape       = new double[vertices * 2];
        double angleStep     = 2 * Math.PI / vertices;

        for (int i = 0; i < vertices; i++) {
            double angle = i * angleStep;
            // jitter each vertex ±20%
            double r = baseRadius * (0.8 + random.nextDouble() * 0.4);

            int idx = i * 2;
            shape[idx]     = Math.cos(angle) * r;
            shape[idx + 1] = Math.sin(angle) * r;
        }

        return shape;
    }

    /*
    public void update(float dt, int width, int height) {
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);

        // screen wrapping
        if (getX() < -radius)              setX(width  + radius);
        else if (getX() > width  + radius) setX(-radius);

        if (getY() < -radius)              setY(height + radius);
        else if (getY() > height + radius) setY(-radius);
    }
     */

    public AsteroidSize getSize() {
        return size;
    }

    @Override
    public Color getBaseColor() {
        return Color.GRAY;
    }

}
