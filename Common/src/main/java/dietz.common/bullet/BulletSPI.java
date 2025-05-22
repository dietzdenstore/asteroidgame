package dietz.common.bullet;

import dietz.common.data.Entity;
import dietz.common.data.GameData;
/**
 * Service Provider Interface for creating and managing bullets in the game.
 */
public interface BulletSPI {
    /**
     * Creates a new bullet entity fired by the given shooter.

     * Preconditions:
     * @param shooter must not be null and must exist in the world.
     * @param gameData must not be null and contain current timing information.

     * Postconditions:
     * A new Bullet entity is created, returned, and added to the world
     *   if the shooter is allowed to fire (e.g., cooldown has elapsed).

     * @return the newly-created Bullet entity
     */
    Entity createBullet(Entity shooter, GameData gameData);
}
