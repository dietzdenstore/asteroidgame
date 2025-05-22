package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;
/**
 * Defines a contract for per-frame processing of world entities,
 * such as movement, input handling, or Enemy logic.
 */
public interface IEntityProcessingService {
    /**
     * Processes all relevant entities for one frame of the game loop.

     * Preconditions:
     * @param gameData must not be null and provide delta time and input state.
     * @param world must not be null and contain entities to process.

     * Postconditions:
     * - Each entity handled by this service has been processed exactly once,
     *   with any changes applied to the world.
     */
    void process(GameData gameData, World world);
}
