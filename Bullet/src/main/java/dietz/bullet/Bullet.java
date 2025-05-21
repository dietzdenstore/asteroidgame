package dietz.bullet;

import dietz.common.data.Entity;
import javafx.scene.paint.Color;

public class Bullet extends Entity {
    private static final float velocity = 700f;
    private static final Color color = Color.DEEPPINK;

    private final double dx, dy;

    public Bullet(double x, double y, double rotation) {
        setX(x);
        setY(y);
        setRotation(rotation);
        setRadius(2f);
        setPolygonCoordinates(1, -1,  1, 1,  -1, 1,  -1, -1);

        // pre-compute velocity vector
        double rad = Math.toRadians(rotation);
        this.dx = Math.cos(rad) * velocity;
        this.dy = Math.sin(rad) * velocity;
    }

    public double getDx()       { return dx; }
    public double getDy()       { return dy; }
    public Color  getBaseColor(){ return color; }
}
