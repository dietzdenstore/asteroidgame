package dietz.asteroid;

import dietz.common.asteroid.AsteroidSize;
import dietz.common.data.Entity;
import dietz.common.data.World;
import dietz.common.asteroid.IAsteroidSplitter;

import java.util.Random;

public class AsteroidSplitter implements IAsteroidSplitter {

    private static final Random random = new Random();

    @Override
    public void createSplitAsteroid(Entity original, World world) {
        if (!(original instanceof Asteroid parent)) return;

        AsteroidSize parentSize = parent.getSize();
        if (!parentSize.canSplit()) return;

        AsteroidSize childSize = parentSize.nextSize();
        int fragments = parentSize.getRandomFragments();

        // fragments
        double parentDx = parent.getDx();
        double parentDy = parent.getDy();
        double parentSpeed = Math.hypot(parentDx, parentDy);
        double parentAngle = Math.toDegrees(Math.atan2(parentDy, parentDx));
        double baseAngle = random.nextDouble() * 360;

        for (int i = 0; i < fragments; i++) {
            if (world.getEntityCount("Asteroid") >= AsteroidFactory.maximumAsteroids) break;
            double angle = baseAngle + i * 360.0 / fragments + (random.nextDouble() - 0.5) * 20; // random spread
            double offset = (parentSize.getRadius() + childSize.getRadius()) / 2.0;
            double x = parent.getX() + Math.cos(Math.toRadians(angle)) * offset;
            double y = parent.getY() + Math.sin(Math.toRadians(angle)) * offset;
            double burstSpeed = childSize.getRadius() * (1 + random.nextDouble());
            double factor = 0.8 + random.nextDouble() * 0.4;
            double dx = Math.cos(Math.toRadians(parentAngle)) * parentSpeed * factor + Math.cos(Math.toRadians(angle)) * burstSpeed;
            double dy = Math.sin(Math.toRadians(parentAngle)) * parentSpeed * factor + Math.sin(Math.toRadians(angle)) * burstSpeed;
            Asteroid child = new Asteroid(childSize, x, y, angle);
            child.setDx(dx);
            child.setDy(dy);
            world.addEntity(child);
        }

        world.removeEntity(parent); // Remove original after split
    }
}