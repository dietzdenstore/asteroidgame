import dietz.common.asteroid.IAsteroidSplitter;

module Asteroid {
    exports dietz.asteroid;
    requires Common;
    requires javafx.graphics;
    requires java.desktop;


    provides dietz.common.services.IGamePlugin with dietz.asteroid.AsteroidPlugin;
    provides dietz.common.services.IEntityProcessing with dietz.asteroid.AsteroidProcessor;
    provides IAsteroidSplitter with dietz.asteroid.AsteroidSplitter;
}