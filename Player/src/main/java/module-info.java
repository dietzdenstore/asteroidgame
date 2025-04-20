module Player {
    requires Bullet;
    requires Common;
    provides dietz.common.GamePlugin with dietz.player.PlayerPlugin;
}
