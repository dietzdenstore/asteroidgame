module Collision {
    requires Common;

    exports dietz.collision;

    uses     dietz.common.data.asteroid.IAsteroidSplitter;

    provides dietz.common.services.IPostEntityProcessing with dietz.collision.CollisionPlugin;
}