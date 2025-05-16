import dietz.common.services.IGamePlugin;

module Common {
    requires java.desktop;
    requires javafx.graphics;

    exports dietz.common.services;
    exports dietz.common.data;
    exports dietz.common.asteroid;
    exports dietz.common.bullet;

    uses dietz.common.services.IGamePlugin;
    uses dietz.common.services.IEntityProcessing;
    uses dietz.common.services.IPostEntityProcessing;
    uses dietz.common.bullet.BulletSPI;
}