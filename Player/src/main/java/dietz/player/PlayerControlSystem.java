package dietz.player;

import java.util.List;
import java.util.ServiceLoader;
import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.GameKeys;
import dietz.common.data.WallCollisionMode;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessingService;
import dietz.common.util.ServiceLocator;

public class PlayerControlSystem implements IEntityProcessingService {
    private final BulletSPI bulletSPI;
    private float shootCooldown = 0f;
    private static final float fireRate = 0.1f;
    private static final double deaccelerationFactor = 0.97;

    public PlayerControlSystem() {
        this.bulletSPI = ServiceLoader.load(BulletSPI.class).findFirst().orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        float dt = gameData.getDeltaTime();

        for (Entity e : world.getEntities(Player.class)) {
            Player p = (Player) e;
            GameKeys keys = gameData.getKeys();

            if (keys.isDown(GameKeys.LEFT)) {
                p.setRotation(p.getRotation() - Player.rotationSpeed * dt);
            }
            if (keys.isDown(GameKeys.RIGHT)) {
                p.setRotation(p.getRotation() + Player.rotationSpeed * dt);
            }
            if (keys.isDown(GameKeys.UP)) {
                double rad = Math.toRadians(p.getRotation());
                p.setDx(p.getDx() + Math.cos(rad) * Player.acceleration * dt);
                p.setDy(p.getDy() + Math.sin(rad) * Player.acceleration * dt);

                double speed = Math.hypot(p.getDx(), p.getDy());
                if (speed > Player.maxSpeed) {
                    double scale = Player.maxSpeed / speed;
                    p.setDx(p.getDx() * scale);
                    p.setDy(p.getDy() * scale);
                }
            } else {
                p.setDx(p.getDx() * Math.pow(deaccelerationFactor, dt * 60));
                p.setDy(p.getDy() * Math.pow(deaccelerationFactor, dt * 60));
            }

            // —— MOVE & HANDLE WALLS ——
            double newX = p.getX() + p.getDx() * dt;
            double newY = p.getY() + p.getDy() * dt;
            double dx   = p.getDx();
            double dy   = p.getDy();
            WallCollisionMode mode = gameData.getWallMode();

            switch (mode) {
                case WRAP:
                    newX = wrap(newX, gameData.getDisplayWidth());
                    newY = wrap(newY, gameData.getDisplayHeight());
                    break;

                case BOUNCE:
                    if (newX < 0 || newX > gameData.getDisplayWidth()) {
                        dx *= -1;
                        newX = Math.max(0, Math.min(newX, gameData.getDisplayWidth()));
                    }
                    if (newY < 0 || newY > gameData.getDisplayHeight()) {
                        dy *= -1;
                        newY = Math.max(0, Math.min(newY, gameData.getDisplayHeight()));
                    }
                    break;

                case STOP:
                    if ((newX < 0 && dx < 0) || (newX > gameData.getDisplayWidth() && dx > 0)) dx = 0;
                    if ((newY < 0 && dy < 0) || (newY > gameData.getDisplayHeight() && dy > 0)) dy = 0;
                    newX = Math.max(0, Math.min(newX, gameData.getDisplayWidth()));
                    newY = Math.max(0, Math.min(newY, gameData.getDisplayHeight()));
                    break;
            }

            p.setX(newX);
            p.setY(newY);
            p.setDx(dx);
            p.setDy(dy);

            shootCooldown -= dt;
            if (bulletSPI != null && keys.isDown(GameKeys.SPACE) && shootCooldown <= 0f) {
                world.addEntity(bulletSPI.createBullet(p, gameData));
                shootCooldown = fireRate;
            }

            keys.update();
        }
    }

    private double wrap(double value, double max) {
        if (value < 0) return value + max;
        if (value > max) return value - max;
        return value;
    }
}
