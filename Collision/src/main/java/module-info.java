module Collision {
    requires Common;
    requires Asteroid;
    requires Bullet;
    requires Enemy;

    exports dietz.collision;

    uses dietz.common.data.asteroid.IAsteroidSplitter;
    provides dietz.common.services.IGamePlugin with dietz.collision.CollisionPlugin;

}