import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IGamePluginService;

module Asteroid {
    exports dietz.asteroid;
    requires Common;
    requires javafx.graphics;
    requires java.desktop;


    provides IGamePluginService with dietz.asteroid.AsteroidPlugin;
    provides IEntityProcessingService with dietz.asteroid.AsteroidProcessor;
    provides IAsteroidSplitter with dietz.asteroid.AsteroidSplitter;
}