package dietz.player;

import dietz.common.*;
import dietz.player.systems.*;
import dietz.bullet.*;

public class PlayerPlugin implements GamePlugin {
    private String playerId;
    private final InputSystem inputSystem = new InputSystem();
    private final MovementSystem movementSystem = new MovementSystem();
    private final BulletSystem bulletSystem = new BulletSystem();

    private float shootCooldown = 0f;
    private static final float FIRE_RATE = 0.2f;
    private static final double DECELERATION = 0.97;; // seconds between shots (5 bullets/sec)


    @Override
    public void start(GameData data, World world) {
        Player p = new Player();
        p.setX(data.getDisplayWidth() / 2.0);
        p.setY(data.getDisplayHeight() / 2.0);
        playerId = world.addEntity(p);
    }

    @Override
    public void update(GameData data, World world) {
        float dt = data.getDeltaTime();
        Player p = (Player) world.getEntity(playerId);
        GameKeys keys = data.getKeys();

        if (inputSystem.isRotatingLeft(keys))
            p.setRotation(p.getRotation() - Player.ROT_SPEED * dt);

        if (inputSystem.isRotatingRight(keys))
            p.setRotation(p.getRotation() + Player.ROT_SPEED * dt);

        if (inputSystem.isAccelerating(keys)) {
            movementSystem.applyThrust(p, dt);
        } else {
            movementSystem.applyDeceleration(p, dt, DECELERATION);
        }

        movementSystem.moveAndHandleWalls(p, dt, data.getDisplayWidth(), data.getDisplayHeight(), data.getWallMode());

        shootCooldown -= dt;
        if (inputSystem.isShooting(keys) && shootCooldown <= 0f) {
            bulletSystem.spawnBullet(p, world);
            shootCooldown = FIRE_RATE; // or data.getFireRate()
        }

        bulletSystem.updateBullets(world, dt, data.getDisplayWidth(), data.getDisplayHeight());

        keys.update();
    }
}
