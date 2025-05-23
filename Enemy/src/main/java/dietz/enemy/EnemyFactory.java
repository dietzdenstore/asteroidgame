package dietz.enemy;

import dietz.common.data.GameData;
import dietz.common.data.World;

import java.util.Random;

public class EnemyFactory {
    private static final Random random = new Random();

    public static void spawnEnemy(GameData gameData, World world, int count) {
        for (int i = 0; i < count; i++) {
            Enemy enemy = new Enemy();
            enemy.setX(random.nextDouble() * gameData.getDisplayWidth());
            enemy.setY(random.nextDouble() * gameData.getDisplayHeight());
            world.addEntity(enemy);
        }
    }
}
