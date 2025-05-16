module Bullet {
    requires Common;
    requires javafx.graphics;

    uses dietz.common.bullet.BulletSPI;


    provides dietz.common.services.IEntityProcessing with dietz.bullet.BulletControlSystem;
    provides dietz.common.services.IGamePlugin with dietz.bullet.BulletPlugin;
    provides dietz.common.bullet.BulletSPI with dietz.bullet.BulletControlSystem;

    exports dietz.bullet;
}