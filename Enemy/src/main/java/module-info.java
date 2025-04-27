module Enemy {
    requires Bullet;
    requires Common;
    provides dietz.common.GamePlugin with dietz.enemy.EnemyPlugin;
}
