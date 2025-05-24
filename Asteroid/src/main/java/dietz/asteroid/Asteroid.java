package dietz.asteroid;

import dietz.common.data.Entity;
import javafx.scene.paint.Color;

import java.util.Random;

public class Asteroid extends Entity {
    private final AsteroidSize size;
    private final float        radius;
    private final double       dx, dy;
    private static final Random random = new Random();
    private static final Color color = Color.GRAY;

    public Asteroid(AsteroidSize size, double x, double y, double direction) {
        this.size = size;

        float base = size.getRadius();
        this.radius = base * (0.8f + random.nextFloat() * 0.4f);
        setPolygonCoordinates(generateShape(this.radius));
        setRadius(this.radius);

        setX(x);
        setY(y);

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

    public AsteroidSize getSize() {
        return size;
    }

    //deriveColor(hue, saturation, brightness, opacity)

    @Override
    public Color getBaseColor() {

        switch (size) {
            case GIANT:
            case LARGE:
                return color.deriveColor(0, 1.0, 0.8, 1.0);
            case MEDIUM:
                return color.deriveColor(0, 1.0, 1.0, 1.0);
            case SMALL:
                return color.deriveColor(0, 1.0, 1.2, 1.0);
            default:
                return color;
        }
    }
}
