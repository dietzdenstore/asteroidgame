module Enemy {
    requires Common;
    uses dietz.common.bullet.BulletSPI;

    provides dietz.common.services.IEntityProcessing with dietz.enemy.EnemyControlSystem;
    provides dietz.common.services.IGamePlugin with dietz.enemy.EnemyPlugin;
}
