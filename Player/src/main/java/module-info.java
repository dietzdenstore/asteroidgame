import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;

module Player {
    requires Common;
    requires javafx.graphics;
    requires java.desktop;

    uses dietz.common.bullet.BulletSPI;

    provides IGamePluginService with dietz.player.PlayerPlugin;
    provides IEntityProcessingService with dietz.player.PlayerControlSystem;
}
