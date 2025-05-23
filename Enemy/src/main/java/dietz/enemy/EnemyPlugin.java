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
        EnemyFactory.spawnEnemy(gameData, world, Enemy.enemyCount);

    }

    // Respawn Enemy
    @Override
    public void stop(GameData gameData, World world) {
        world.getEntities(Enemy.class).forEach(world::removeEntity);
    }


}
