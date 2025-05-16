import dietz.common.services.IEntityProcessing;
import dietz.common.services.IGamePlugin;

module Player {
    requires Common;

    uses dietz.common.bullet.BulletSPI;

    provides IGamePlugin with dietz.player.PlayerPlugin;
    provides IEntityProcessing with dietz.player.PlayerControlSystem;
}
