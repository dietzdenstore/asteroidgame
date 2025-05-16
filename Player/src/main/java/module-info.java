import dietz.common.services.IEntityProcessing;
import dietz.common.services.IGamePlugin;

module Player {
    requires Common;
    requires javafx.graphics;
    requires java.desktop;

    uses dietz.common.bullet.BulletSPI;

    provides IGamePlugin with dietz.player.PlayerPlugin;
    provides IEntityProcessing with dietz.player.PlayerControlSystem;
}
