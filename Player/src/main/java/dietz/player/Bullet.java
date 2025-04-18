package dietz.player;

import dietz.common.Entity;

public class Bullet extends Entity {
    private static final double SPEED = 500; // px/sec
    private final double dx, dy;

    public Bullet(double x, double y, double rotDeg) {
        // small square
        setPolygonCoordinates(-2,-2, 2,-2, 2,2, -2,2);
        setRadius(2f);
        setX(x);
        setY(y);
        setRotation(rotDeg);

        double rad = Math.toRadians(rotDeg);
        dx = Math.cos(rad) * SPEED;
        dy = Math.sin(rad) * SPEED;
    }

    public void update(float dt, int w, int h) {
        setX(wrap(getX() + dx*dt, w));
        setY(wrap(getY() + dy*dt, h));
    }

    private double wrap(double val, double max) {
        if (val < 0)   return val + max;
        if (val > max) return val - max;
        return val;
    }
}
