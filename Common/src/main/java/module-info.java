import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;
import dietz.common.services.IPostEntityProcessingService;

module Common {
    requires java.desktop;
    requires javafx.graphics;
    requires java.prefs;

    exports dietz.common.services;
    exports dietz.common.data;
    exports dietz.common.asteroid;
    exports dietz.common.bullet;
    exports dietz.common.ship;
    exports dietz.common.components;
    exports dietz.common.util;

    uses IGamePluginService;
    uses IEntityProcessingService;
    uses IPostEntityProcessingService;
    uses dietz.common.bullet.BulletSPI;
    uses dietz.common.asteroid.IAsteroidSplitter;
}