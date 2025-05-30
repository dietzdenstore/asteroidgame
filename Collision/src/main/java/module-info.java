import dietz.collision.CollisionSystem;
import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;

    exports dietz.collision;

    uses IAsteroidSplitter;

    provides IPostEntityProcessingService with CollisionSystem;
}