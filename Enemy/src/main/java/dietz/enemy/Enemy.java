package dietz.enemy;

import dietz.common.data.Entity;
import dietz.common.components.Health;
import dietz.common.ship.ShipDesign;
import javafx.scene.paint.Color;

public class   Enemy extends Entity {
    public static final double acceleration    = 300;
    public static final double maxSpeed        = 400;
    public static final double rotationSpeed   = 250;

    // AI & shooting parameters
    public static final float  changeInterval  = 1.0f;
    public static final float  fireRate        = 0.05f;
    public static final double shootChance     = 0.02;

    public static final int     enemyCount   = 5;
    public static final int     maxEnemies   = 7;
    public static final float   respawnDelay = 3f;

    public static final Color color = Color.RED;

    private static ShipDesign design;

    private double dx = 0, dy = 0;

    public Enemy() {
        design = ShipDesign.random();
        setPolygonCoordinates(design.getShape());
        setRadius(10f);
        setRotation(0);
        addComponent(new Health(1));
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }

    @Override
    public Color getBaseColor() {
        switch (design) {
            case CLASSIC:
                return color.brighter();
            case ARROWHEAD:
                return color.brighter();
            case STEALTH:
                return color.darker();
            case TRIDENT:
                return color.darker();
            case PHANTOM:
                return color.brighter();
            default:
                return color;
        }
    }
}

