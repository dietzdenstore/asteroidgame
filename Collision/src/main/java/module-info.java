import dietz.common.asteroid.IAsteroidSplitter;

module Collision {
    requires Common;

    exports dietz.collision;

    uses IAsteroidSplitter;

    provides dietz.common.services.IPostEntityProcessing with dietz.collision.CollisionPlugin;
}