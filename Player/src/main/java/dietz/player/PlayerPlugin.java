package dietz.player;

import dietz.common.*;
import dietz.common.services.IGamePlugin;
import dietz.player.systems.*;
import dietz.bullet.*;

public class PlayerPlugin implements IGamePlugin {
    private String playerId;
    private final InputSystem inputSystem = new InputSystem();
    private final MovementSystem movementSystem = new MovementSystem();
    private final BulletSystem bulletSystem = new BulletSystem();

    private float shootCooldown = 0f;
    private static final float FIRE_RATE = 0.2f;
    private static final double DECELERATION = 0.97;; // seconds between shots (5 bullets/sec)


    @Override
    public void start(GameData gameData, World world) {
        Player player = new Player();
        player.setX(gameData.getDisplayWidth() / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        playerId = world.addEntity(player);
    }

    @Override
    public void update(GameData gameData, World world) {
        float dt = gameData.getDeltaTime();
        Player player = (Player) world.getEntity(playerId);
        GameKeys keys = gameData.getKeys();

        if (inputSystem.isRotatingLeft(keys))
            player.setRotation(player.getRotation() - Player.ROT_SPEED * dt);

        if (inputSystem.isRotatingRight(keys))
            player.setRotation(player.getRotation() + Player.ROT_SPEED * dt);

        if (inputSystem.isAccelerating(keys)) {
            movementSystem.applyThrust(player, dt);
        } else {
            movementSystem.applyDeceleration(player, dt, DECELERATION);
        }

        movementSystem.moveAndHandleWalls(player, dt, gameData.getDisplayWidth(), gameData.getDisplayHeight(), gameData.getWallMode());

        shootCooldown -= dt;
        if (inputSystem.isShooting(keys) && shootCooldown <= 0f) {
            bulletSystem.spawnBullet(player, world);
            shootCooldown = FIRE_RATE; // or gameData.getFireRate()
        }

        bulletSystem.updateBullets(world, dt, gameData.getDisplayWidth(), gameData.getDisplayHeight());

        keys.update();
    }

    @Override
    public void stop(GameData gameData, World world) {

    }
}
