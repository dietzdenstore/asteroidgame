module Asteroid {
    exports dietz.asteroid;
    requires Common;


    provides dietz.common.services.IGamePlugin with dietz.asteroid.AsteroidPlugin;
    provides dietz.common.services.IEntityProcessing with dietz.asteroid.AsteroidProcessor;
    provides dietz.common.data.asteroid.IAsteroidSplitter with dietz.asteroid.AsteroidSplitter;
}