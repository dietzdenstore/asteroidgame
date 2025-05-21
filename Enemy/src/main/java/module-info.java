import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;

module Enemy {
    requires Common;
    requires javafx.graphics;
    uses dietz.common.bullet.BulletSPI;

    provides IEntityProcessingService with dietz.enemy.EnemyControlSystem;
    provides IGamePluginService with dietz.enemy.EnemyPlugin;
}
