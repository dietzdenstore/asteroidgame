package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;
/**
 * Defines cleanup and resolution tasks after primary processing,
 * such as collision detection, splitting, and removal of entities.
 */
public interface IPostEntityProcessingService {
    /**
     * Runs post-processing logic once per game loop iteration.

     * Preconditions:
     * @param gameData must not be null.
     * @param world must not be null and reflect state after primary processing.

     * Postconditions:
     * - All necessary cleanup, collision resolution, and spawned actions have been
     *   applied to the world, ensuring consistency of entity lifecycle.
     */
    void process(GameData gameData, World world);
}
