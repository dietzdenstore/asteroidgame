import dietz.common.services.IGamePlugin;

module Enemy {
    requires Bullet;
    requires Common;
    provides IGamePlugin with dietz.enemy.EnemyPlugin;
}
