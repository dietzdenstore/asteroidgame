package dietz.bullet;

import java.util.ArrayList;
import java.util.List;

import dietz.common.bullet.Bullet;
import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessing;

public class BulletControlSystem implements IEntityProcessing, BulletSPI {

    private static final float velocity = 700f;

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Bullet b = new Bullet();
        b.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        b.setRadius(1f);

        double dx = Math.cos(Math.toRadians(shooter.getRotation()));
        double dy = Math.sin(Math.toRadians(shooter.getRotation()));

        float safeDist = shooter.getRadius() + b.getRadius() + 2; // 2 px margin
        b.setX(shooter.getX() + dx * safeDist);
        b.setY(shooter.getY() + dy * safeDist);
        b.setRotation(shooter.getRotation());
        return b;
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

            /*
            // expire off‐screen
            if (e.getX() < 0 || e.getX() > w ||
                    e.getY() < 0 || e.getY() > h) {
                toRemove.add(e);
            } */

        }
        toRemove.forEach(world::removeEntity);
    }
}
