module Enemy {
    requires Common;
    requires javafx.graphics;
    uses dietz.common.bullet.BulletSPI;

    provides dietz.common.services.IEntityProcessing with dietz.enemy.EnemyControlSystem;
    provides dietz.common.services.IGamePlugin with dietz.enemy.EnemyPlugin;
}
