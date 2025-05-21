// Enemy/src/main/java/dietz/enemy/EnemyPlugin.java
package dietz.enemy;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePlugin;

import java.util.Random;

public class EnemyPlugin implements IGamePlugin {
    private String enemyId;

    private static final float RESPAWN_DELAY = 10f; // seconds

    private float respawnTimer = 0f;
    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
        spawnEnemy(gameData, world);
    }

    // Respawn Enemy
    @Override
    public void update(GameData gameData, World world) {
        if (!world.getEntities(Enemy.class).isEmpty()) {
            respawnTimer = 0f;
            return;
        }
        respawnTimer += gameData.getDeltaTime();
        if (respawnTimer >= RESPAWN_DELAY) {
            spawnEnemy(gameData, world);
            respawnTimer = 0f;
        }
    }

    private void spawnEnemy(GameData gameData, World world) {
        Enemy enemy = new Enemy();
        enemy.setX(random.nextDouble() * gameData.getDisplayWidth());
        enemy.setY(random.nextDouble() * gameData.getDisplayHeight());
        world.addEntity(enemy);
    }
}
