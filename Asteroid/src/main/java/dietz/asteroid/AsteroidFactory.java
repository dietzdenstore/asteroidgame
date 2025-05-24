package dietz.asteroid;

import dietz.common.data.GameData;
import dietz.common.data.World;

import java.util.Random;


public class AsteroidFactory {

    private static int initialAsteroids = 3;
    static final int maximumAsteroids = 150;
    private static final double spawnBuffer = 200.0;
    private static final Random random = new Random();


    public static void spawnAsteroid(GameData data, World world, AsteroidSize size) {
        if (world.getEntityCount("Asteroid") >= maximumAsteroids) return;
        double[] pos = getEdgePosition(data, size.getRadius());

        // clamp to buffer circle around (0,0)
        double dist2 = pos[0] * pos[0] + pos[1] * pos[1];
        if (dist2 < spawnBuffer * spawnBuffer) {
            double dist = Math.sqrt(dist2);
            double scale = (spawnBuffer + size.getRadius()) / (dist == 0 ? 1 : dist);
            pos[0] *= scale;
            pos[1] *= scale;
        }

        double angle = random.nextDouble() * 360;
        Asteroid asteroid = new Asteroid(size, pos[0], pos[1], angle);

        double speed = 50 + random.nextDouble() * 100;
        asteroid.setDx(Math.cos(Math.toRadians(angle)) * speed);
        asteroid.setDy(Math.sin(Math.toRadians(angle)) * speed);

        world.addEntity(asteroid);
    }

    private static double[] getEdgePosition(GameData data, double radius) {
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

    public static AsteroidSize getRandomSize() {
        double rand = Math.random();
        if (rand < 0.5) return AsteroidSize.GIANT;
        else if (rand < 0.7) return AsteroidSize.LARGE;
        else if (rand < 0.9) return AsteroidSize.MEDIUM;
        else return AsteroidSize.SMALL;
    }

    public static int getInitialAsteroids() {
        return initialAsteroids;
    }
}
