import dietz.common.services.IGamePlugin;

module Enemy {
    exports dietz.enemy;
    requires Bullet;
    requires Common;
    provides IGamePlugin with dietz.enemy.EnemyPlugin;
}
