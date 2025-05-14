package dietz.enemy;

import dietz.bullet.BulletSystem;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.services.IGamePlugin;
import dietz.common.data.World;
import dietz.enemy.systems.AIDecisionSystem;
import dietz.enemy.systems.MovementSystem;

import java.util.Random;

public class EnemyPlugin implements IGamePlugin {
    // Add these new fields
    private final AIDecisionSystem aiDecisionSystem = new AIDecisionSystem();
    private final Random random = new Random();
    private String enemyId;
    private final MovementSystem movementSystem = new MovementSystem();
    private final BulletSystem bulletSystem = new BulletSystem();

    @Override
    public void start(GameData data, World world) {
        Enemy e = new Enemy();
        e.setX(data.getDisplayWidth() / 2.0);
        e.setY(data.getDisplayHeight() / 2.0);
        enemyId = world.addEntity(e);
    }

    @Override
    public void update(GameData data, World world) {
        float dt = data.getDeltaTime();
        Enemy enemy = (Enemy) world.getEntity(enemyId);

        if(enemy == null){
            return;
        }

        // Find player entity (you'll need to implement this)
        Entity player = findPlayerEntity(world);

        // AI-controlled movement
        aiDecisionSystem.updateAI(enemy, player, dt);

        // Auto-shooting
        if (aiDecisionSystem.shouldShoot(random)) {
            bulletSystem.spawnBullet(enemy, world);
        }

        movementSystem.moveAndHandleWalls(enemy, dt,
                data.getDisplayWidth(), data.getDisplayHeight(),
                data.getWallMode());

        bulletSystem.updateBullets(world, dt,
                data.getDisplayWidth(), data.getDisplayHeight());
    }

    private Entity findPlayerEntity(World world) {
        // Implement logic to find player entity
        for (Entity entity : world.getEntities()) {
            if (entity.getClass().getSimpleName().equals("Player")) {
                return entity;
            }
        }
        return null;
    }
}