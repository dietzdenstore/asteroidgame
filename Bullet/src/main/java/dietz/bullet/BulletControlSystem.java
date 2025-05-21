package dietz.bullet;

import java.util.ArrayList;
import java.util.List;

import dietz.common.bullet.Bullet;
import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private static final float velocity = 700f;

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet bullet = new Bullet();
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        bullet.setRadius(1f);

        double dx = Math.cos(Math.toRadians(shooter.getRotation()));
        double dy = Math.sin(Math.toRadians(shooter.getRotation()));

        float safeDist = shooter.getRadius() + bullet.getRadius() + 2; // 2 px margin
        bullet.setX(shooter.getX() + dx * safeDist);
        bullet.setY(shooter.getY() + dy * safeDist);
        bullet.setRotation(shooter.getRotation());
        return bullet;
    }

    @Override
    public void process(GameData gameData, World world) {
        float dt = gameData.getDeltaTime();
        int w   = gameData.getDisplayWidth();
        int h   = gameData.getDisplayHeight();

        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities(Bullet.class)) {

            double dx = Math.cos(Math.toRadians(e.getRotation()));
            double dy = Math.sin(Math.toRadians(e.getRotation()));
            e.setX(e.getX() + dx * velocity * dt);
            e.setY(e.getY() + dy * velocity * dt);
        }
        toRemove.forEach(world::removeEntity);
    }
}
