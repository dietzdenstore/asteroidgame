package dietz.bullet;

import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import javafx.scene.paint.Color;

public class BulletFactory implements BulletSPI {

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        double rotation = shooter.getRotation();
        double x   = shooter.getX() + Math.cos(Math.toRadians(rotation)) * shooter.getRadius()*2;
        double y   = shooter.getY() + Math.sin(Math.toRadians(rotation)) * shooter.getRadius()*2;

        Color bulletColor = "Player".equals(shooter.getType()) ? Color.LIMEGREEN : Color.RED;
        return new Bullet(x, y, rotation, bulletColor);

    }
}
