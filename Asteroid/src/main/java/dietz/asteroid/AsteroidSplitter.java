package dietz.asteroid;

import dietz.common.data.Entity;
import dietz.common.data.World;
import dietz.common.data.asteroid.IAsteroidSplitter;

import java.util.Random;

public class AsteroidSplitter implements IAsteroidSplitter {

    private static final Random random = new Random();

    @Override
    public void createSplitAsteroid(Entity original, World world) {

        // only operate on real asteroids
        if (!(original instanceof Asteroid big)) return;

        Asteroid.Size size = big.getSize();
        Asteroid.Size childSize =
                switch (size) {
                    case LARGE  -> Asteroid.Size.MEDIUM;
                    case MEDIUM -> Asteroid.Size.SMALL;
                    case SMALL  -> null;           // can’t split further
                };
        if (childSize == null) return;

        double x = big.getX(), y = big.getY();
        float  r = big.getRadius() / 2f;

        for (int i = 0; i < 2; i++) {
            double dir = random.nextDouble() * 360;
            Asteroid child = new Asteroid(childSize, x, y, dir);
            child.setRadius(r);
            world.addEntity(child);
        }
    }
}