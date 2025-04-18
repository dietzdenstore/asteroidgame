package dietz.player;

import dietz.common.Entity;

public class Player extends Entity {
    public static final double ACCEL     = 200;  // px/sec²
    public static final double MAX_SPEED = 300;  // px/sec
    public static final double ROT_SPEED = 180;  // deg/sec

    private double dx = 0, dy = 0;

    public Player() {
        // narrow triangle pointing up
        setPolygonCoordinates(0, -10,  7, 10,  -7, 10);
        setRadius(10f);
        setRotation(0);
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}
