package dietz.asteroid;

import dietz.common.asteroid.AsteroidSize;
import dietz.common.data.GameData;
import dietz.common.services.IGamePlugin;
import dietz.common.data.World;

public class AsteroidPlugin implements IGamePlugin {

    private int initialAsteroids = 3;
    static final int maximumAsteroids = 400;
    private float spawnTimer = 0;
    private static final float SPAWN_INTERVAL = 7f; // Spawn every 10 seconds

    @Override
    public void start(GameData gameData, World world) {
        // Initial spawn of 6 large asteroids
        for (int i = 0; i < initialAsteroids; i++) {
            spawnAsteroid(gameData, world, AsteroidSize.GIANT);
        }
    }

    @Override
    public void update(GameData data, World world) {
        // Periodic spawning of random-sized asteroids
        spawnTimer += data.getDeltaTime();
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0;
            spawnAsteroid(data, world, getRandomSize());
        }
    }

    private void spawnAsteroid(GameData data, World world, AsteroidSize size) {
        if (world.getEntityCount("Asteroid") >= maximumAsteroids) return;
        // Spawn at screen edge with direction toward center
        double[] pos = getEdgePosition(data, size.getRadius());
        double angle = calculateAngleToCenter(pos[0], pos[1], data);

        Asteroid asteroid = new Asteroid(size, pos[0], pos[1], angle);
        world.addEntity(asteroid);
    }

    private double[] getEdgePosition(GameData data, double radius) {
        // Randomly select screen edge
        int edge = (int) (Math.random() * 4);
        double x, y;
        double width = data.getDisplayWidth();
        double height = data.getDisplayHeight();

        switch (edge) {
            case 0: // Top
                x = Math.random() * width;
                y = -radius;
                break;
            case 1: // Right
                x = width + radius;
                y = Math.random() * height;
                break;
            case 2: // Bottom
                x = Math.random() * width;
                y = height + radius;
                break;
            default: // Left
                x = -radius;
                y = Math.random() * height;
        }
        return new double[]{x, y};
    }

    private double calculateAngleToCenter(double x, double y, GameData data) {
        // Calculate direction toward screen center
        double centerX = data.getDisplayWidth() / 2.0;
        double centerY = data.getDisplayHeight() / 2.0;
        return Math.toDegrees(Math.atan2(centerY - y, centerX - x));
    }

    private AsteroidSize getRandomSize() {
        // Weighted random selection (higher chance for larger asteroids)
        double rand = Math.random();
        if (rand < 0.6) return AsteroidSize.LARGE;
        else if (rand < 0.9) return AsteroidSize.MEDIUM;
        else return AsteroidSize.SMALL;
    }
}