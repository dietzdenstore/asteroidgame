package dietz.bullet;

import dietz.common.Entity;
import dietz.common.World;
import dietz.bullet.Bullet;
import dietz.bullet.BulletConfig;

public class BulletSystem {
    private final BulletConfig defaultConfig = new BulletConfig();

    public void spawnBullet(Entity shooter, World world) {
        spawnBullet(shooter, defaultConfig, world);
    }

    public void spawnBullet(Entity shooter, BulletConfig config, World world) {
        Bullet b = new Bullet(shooter.getX(), shooter.getY(), shooter.getRotation(), config);
        world.addEntity(b);
    }

    public void updateBullets(World world, float dt, int w, int h) {
        for (Entity e : world.getEntities(Bullet.class)) {
            ((Bullet) e).update(dt, w, h);
        }
    }
}
