package dietz.player;

import dietz.common.data.Entity;
import dietz.common.components.Health;
import dietz.common.ship.ShipDesign;
import javafx.scene.paint.Color;

public class Player extends Entity {
    public static final double acceleration = 300;  // px/sec²
    public static final double maxSpeed = 400;  // px/sec
    public static final double rotationSpeed = 250;  // deg/sec
    public static final Color color = Color.NAVY;  // sec

    private double dx = 0, dy = 0;

    public Player() {
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
        addComponent(new Health(3));
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    @Override
    public Color getBaseColor() {
        return color;
    }
}
