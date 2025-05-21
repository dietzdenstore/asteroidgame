package dietz.enemy;

import java.util.ServiceLoader;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.WallCollisionMode;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessingService;

public class EnemyControlSystem implements IEntityProcessingService {
    private final BulletSPI bulletSPI;
    private final Random random = new Random();

    private final Map<String, Float> dirTimers    = new HashMap<>();
    private final Map<String, Double> dirAngles   = new HashMap<>();
    private final Map<String, Float>   shootCd    = new HashMap<>();

    private static final float changeInterval = 1.0f;
    private static final float moveSpeed = 100.0f;
    private static final float fireRate = 0.05f;
    private static final double shootChance = 0.02;

    public EnemyControlSystem() {
        this.bulletSPI = ServiceLoader
                .load(BulletSPI.class)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void process(GameData gameData, World world) {
        float dt     = gameData.getDeltaTime();
        int   w      = gameData.getDisplayWidth();
        int   h      = gameData.getDisplayHeight();
        WallCollisionMode mode = gameData.getWallMode();

        for (Entity e : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) e;
            String id   = enemy.getID();
            float t = dirTimers.getOrDefault(id, 0f) - dt;
            if (t <= 0) {
                double angle = random.nextDouble() * 360;
                dirAngles.put(id, angle);
                t = changeInterval;
            }
            dirTimers.put(id, t);
            double angleRad = Math.toRadians(dirAngles.get(id));
            double dx = Math.cos(angleRad) * moveSpeed * dt;
            double dy = Math.sin(angleRad) * moveSpeed * dt;

            double nx = enemy.getX() + dx;
            double ny = enemy.getY() + dy;

            // — Wall handling —
            switch (mode) {
                case WRAP:
                    if (nx < 0) nx += w;
                    else if (nx > w) nx -= w;
                    if (ny < 0) ny += h;
                    else if (ny > h) ny -= h;
                    break;
                case BOUNCE:
                    if (nx < 0 || nx > w) {
                        dx = -dx;
                        nx = Math.max(0, Math.min(nx, w));
                    }
                    if (ny < 0 || ny > h) {
                        dy = -dy;
                        ny = Math.max(0, Math.min(ny, h));
                    }
                    break;
                case STOP:
                    if (nx < 0) nx = 0;
                    if (nx > w) nx = w;
                    if (ny < 0) ny = 0;
                    if (ny > h) ny = h;
                    break;
            }

            enemy.setX(nx);
            enemy.setY(ny);
            enemy.setRotation(dirAngles.get(id));

            // Shooting cooldown
            float sc = shootCd.getOrDefault(id, 0f) - dt;
            if (sc < 0) sc = 0;

            // random shoot chance
            if (bulletSPI != null && sc <= 0 && random.nextDouble() < shootChance) {
                world.addEntity(bulletSPI.createBullet(enemy, gameData));
                sc = fireRate;
            }
            shootCd.put(id, sc);
        }
    }
}
