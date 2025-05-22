package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;

/**
 * Defines lifecycle methods for a game plugin.
 * Responsible for initializing and updating game world components each loop.
 */
public interface IGamePluginService {
    /**
     * Called once when the plugin is first loaded.

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
     * - The plugin's entities in the world have been updated according to gameData.
     */
    void update(GameData gameData, World world);

}
