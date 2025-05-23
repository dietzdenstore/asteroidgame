package dietz.asteroid;

import dietz.common.asteroid.AsteroidSize;
import dietz.common.data.GameData;
import dietz.common.services.IGamePluginService;
import dietz.common.data.World;

import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {


    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < AsteroidFactory.getInitialAsteroids(); i++) {
            AsteroidFactory.spawnAsteroid(gameData, world, AsteroidSize.GIANT);
        }
    }

    @Override
    public void stop(GameData data, World world) {
        world.getEntities(Asteroid.class).forEach(world::removeEntity);
    }



}