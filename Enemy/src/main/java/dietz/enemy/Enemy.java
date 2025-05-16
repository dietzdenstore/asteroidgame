package dietz.enemy;

import dietz.common.data.Entity;
import dietz.common.data.Health;
import dietz.enemy.systems.ShipDesign;
import javafx.scene.paint.Color;

/**
 * Simple data‐only entity.
 * ACCEL, MAX_SPEED and ROT_SPEED are used by EnemyControlSystem.
 */
public class    Enemy extends Entity {
    public static final double ACCEL     = 200;  // px/sec²
    public static final double MAX_SPEED = 300;  // px/sec
    public static final double ROT_SPEED = 180;  // deg/sec

    // velocity components
    private double dx = 0, dy = 0;

    public Enemy() {
        // pick a random ship shape
        ShipDesign design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);

        // give it some health
        addComponent(new Health(3));

        // so EnemyControlSystem can find it by type if you need
        setType("Enemy");
    }

    // getters/setters for movement
    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    @Override
    public Color getBaseColor() {
        return Color.DARKRED;
    }
}
