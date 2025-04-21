package dietz.bullet;

import dietz.common.Entity;

public class Bullet extends Entity {
    private double dx, dy;
    private float timeAlive = 0;
    private final float maxLifetime = 2.0f; // in seconds

    public Bullet(double x, double y, double rotDeg, BulletConfig config) {
        setPolygonCoordinates(-2, -2, 2, -2, 2, 2, -2, 2);
        setRadius(config.getRadius());
        setX(x);
        setY(y);
        setRotation(rotDeg);

        double rad = Math.toRadians(rotDeg);
        dx = Math.cos(rad) * config.getSpeed();
        dy = Math.sin(rad) * config.getSpeed();
    }

    public boolean isExpired() {
        return timeAlive >= maxLifetime;
    }

    public void update(float dt, int w, int h) {
        timeAlive += dt;
        setX(getX() + dx * dt);
        setY(getY() + dy * dt);
    }
}
