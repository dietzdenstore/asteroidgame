import dietz.common.services.IGamePlugin;

module Player {
    requires Bullet;
    requires Common;
    provides IGamePlugin with dietz.player.PlayerPlugin;
}
