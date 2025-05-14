package dietz.asteroid;

import dietz.common.data.Entity;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Asteroid extends Entity {
    public enum Size {
        LARGE(30, 2, 150),
        MEDIUM(20, 1, 250),
        SMALL(10, 0, 350);

        public final float radius;
        public final int children;
        public final float speed;

        Size(float radius, int children, float speed) {
            this.radius = radius;
            this.children = children;
            this.speed = speed;
        }
    }

    private final Size size;
    private double dx;
    private double dy;

    public Asteroid(Size size, double x, double y, double direction) {
        this.size = size;
        setPolygonCoordinates(generateShape());
        setRadius(size.radius);
        setX(x);
        setY(y);
        double rad = Math.toRadians(direction);
        this.dx = Math.cos(rad) * size.speed;
        this.dy = Math.sin(rad) * size.speed;
    }

    private double[] generateShape() {
        Random random = new Random();
        int points = 8 + random.nextInt(8);              // 8–15 vertices
        List<Double> list = new ArrayList<>(points * 2);

        double baseStep = (Math.PI * 2.0) / points;
        double angle = 0;

        for (int i = 0; i < points; i++) {
            angle += baseStep + (random.nextDouble() - 0.2) * 0.4 * baseStep; // –0.2…+0.2 of baseStep
            double r = size.radius * (0.5 + random.nextDouble());             // 0.5–1.5×radius
            list.add(Math.cos(angle) * r);
            list.add(Math.sin(angle) * r);
        }

        double[] coords = new double[list.size()];
        for (int i = 0; i < coords.length; i++) coords[i] = list.get(i);
        return coords;
    }

    public void update(float dt, int width, int height) {
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);
        // screen wrapping
        if (getX() < -size.radius) setX(width + size.radius);
        else if (getX() > width + size.radius) setX(-size.radius);
        if (getY() < -size.radius) setY(height + size.radius);
        else if (getY() > height + size.radius) setY(-size.radius);
    }

    public Size getSize() {
        return size;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}