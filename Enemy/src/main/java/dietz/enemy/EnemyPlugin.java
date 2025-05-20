// Enemy/src/main/java/dietz/enemy/EnemyPlugin.java
package dietz.enemy;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePlugin;

public class EnemyPlugin implements IGamePlugin {
    private String enemyId;

    @Override
    public void start(GameData gameData, World world) {
        Enemy enemy = new Enemy();
        // spawn in the center (or wherever you like)
        enemy.setX(gameData.getDisplayWidth()  / 2.0 * Math.random());
        enemy.setY(gameData.getDisplayHeight() / 2.0 * Math.random());
        enemyId = world.addEntity(enemy);
    }

    @Override
    public void update(GameData gameData, World world) {
        // all per‐frame work lives in EnemyControlSystem
    }
}
