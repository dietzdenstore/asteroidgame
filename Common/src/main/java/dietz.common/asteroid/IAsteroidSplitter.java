package dietz.common.asteroid;

import dietz.common.data.Entity;
import dietz.common.data.World;

/**
 * Service Provider Interface for splitting asteroids upon destruction.
 */
public interface IAsteroidSplitter {
    /**
     * Splits the original asteroid into smaller fragments.

     * Preconditions:
     * @param original must not be null and represent a splittable asteroid entity.
     * @param world must not be null.

     * Postconditions:
     * - The original asteroid is removed from the world.
     * - Two or more smaller asteroid fragments have been created and added to the world.
     */
    void createSplitAsteroid(Entity original, World world);
}
