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

        // Check if splitting is allowed
        if (!parentSize.canSplit()) return;

        // Get child size and fragment count
        AsteroidSize childSize = parentSize.nextSize();
        int fragments = parentSize.getRandomFragments();


        // Create fragments
        double baseAngle = random.nextDouble() * 360;
        for (int i = 0; i < fragments; i++) {
            if (world.getEntityCount("Asteroid") >= AsteroidPlugin.maximumAsteroids) {break;}
            double angle = baseAngle + (i * 360.0 / fragments);
            double offset =  (parentSize.getRadius() + childSize.getRadius())/2;

            double x = parent.getX() + Math.cos(Math.toRadians(angle)) * offset;
            double y = parent.getY() + Math.sin(Math.toRadians(angle)) * offset;

            // Inherit 50% parent velocity + radial burst
            double burstSpeed = childSize.getRadius() * 2;
            double dx = parent.getDx() * 0.5 + Math.cos(Math.toRadians(angle)) * burstSpeed;
            double dy = parent.getDy() * 0.5 + Math.sin(Math.toRadians(angle)) * burstSpeed;

            Asteroid child = new Asteroid(childSize, x, y, angle);
            child.setDx(dx);
            child.setDy(dy);
            world.addEntity(child);
        }

        world.removeEntity(parent); // Remove original after split
    }
}