import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;

module Bullet {
    requires Common;
    requires javafx.graphics;

    uses dietz.common.bullet.BulletSPI;


    provides IEntityProcessingService with dietz.bullet.BulletControlSystem;
    provides IGamePluginService with dietz.bullet.BulletPlugin;
    provides dietz.common.bullet.BulletSPI with dietz.bullet.BulletControlSystem;

    exports dietz.bullet;
}