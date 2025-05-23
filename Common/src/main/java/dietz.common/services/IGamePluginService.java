package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;

/**
 * Defines lifecycle methods for a game plugin.
 * Responsible for initializing and removing game world components.
 */
public interface IGamePluginService {
    /**
     * Called once when the game ends to clean up and remove this plugin’s entities from the world.

     * Preconditions:
     * @param gameData must not be null and contain valid game state information.
     * @param world must not be null and represent the current game world.

     * Postconditions:
     * - The plugin has added its required initial entities or resources into the world.
     */
    void start(GameData gameData, World world);

    /**
     * Called on each game loop iteration to update the plugin's behavior.

     * Preconditions:
     * @param gameData must not be null and contain current frame timing and input state.
     * @param world must not be null and reflect the game world state before this update.

     * Postconditions:
     * - The plugin's entities in the world have been removed from world.
     */
    void stop(GameData gameData, World world);

}
