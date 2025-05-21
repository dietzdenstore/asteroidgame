package dietz.enemy;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePluginService;

import java.util.Random;

public class EnemyPlugin implements IGamePluginService {
private float respawnTimer = 0f;

    private final Random random = new Random();

    @Override
    public void start(GameData gameData, World world) {
            spawnEnemy(gameData, world, Enemy.enemyCount);

    }

    // Respawn Enemy
    @Override
    public void update(GameData gameData, World world) {
        respawnTimer += gameData.getDeltaTime();
        if (respawnTimer >= Enemy.respawnDelay) {
            int existing = world.getEntities(Enemy.class).size();
            if (existing < Enemy.maxEnemies) {
                int toSpawn = random.nextInt(3) + 1;
                toSpawn = Math.min(toSpawn, Enemy.maxEnemies - existing);

                spawnEnemy(gameData, world, toSpawn);
            }
            respawnTimer = 0f;
        }
    }

    private void spawnEnemy(GameData gameData, World world, int count) {
        for (int i = 0; i < count; i++) {
            Enemy enemy = new Enemy();
            enemy.setX(random.nextDouble() * gameData.getDisplayWidth());
            enemy.setY(random.nextDouble() * gameData.getDisplayHeight());
            world.addEntity(enemy);
        }
    }
}
