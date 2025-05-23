package dietz.asteroid;

import dietz.common.asteroid.AsteroidSize;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessingService;

import java.util.Random;


public class AsteroidProcessor implements IEntityProcessingService {
    private int initialAsteroids = 3;
    static final int maximumAsteroids = 150;
    private static final double spawnBuffer = 200.0;
    private static final Random random = new Random();
    private float spawnTimer = 0;
    private static final float spawnInterval = 3f;

    @Override
    public void process(GameData gameData, World world) {
        for(Entity entity : world.getEntities(Asteroid.class)) {
            Asteroid asteroid = (Asteroid) entity;

            // Update position
            asteroid.setX(asteroid.getX() + asteroid.getDx() * gameData.getDeltaTime());
            asteroid.setY(asteroid.getY() + asteroid.getDy() * gameData.getDeltaTime());

            // Screen wrapping
            wrapAroundScreen(asteroid, gameData);
        }
        spawnTimer += gameData.getDeltaTime();
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0;
            AsteroidFactory.spawnAsteroid(gameData, world, AsteroidFactory.getRandomSize());
        }
    }

    private void wrapAroundScreen(Asteroid asteroid, GameData gameData) {
        double x = asteroid.getX();
        double y = asteroid.getY();
        double radius = asteroid.getRadius();

        if(x < -radius) x = gameData.getDisplayWidth() + radius;
        if(x > gameData.getDisplayWidth() + radius) x = -radius;
        if(y < -radius) y = gameData.getDisplayHeight() + radius;
        if(y > gameData.getDisplayHeight() + radius) y = -radius;

        asteroid.setX(x);
        asteroid.setY(y);
    }


}