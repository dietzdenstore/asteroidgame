// src/main/java/dietz/player/PlayerPlugin.java
package dietz.player;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePlugin;

public class PlayerPlugin implements IGamePlugin {
    private String playerId;

    @Override
    public void start(GameData gameData, World world) {
        // Create and configure the player entity
        Player player = new Player();
        player.setX(gameData.getDisplayWidth()  / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        player.setType("Player");  // so systems can find it by tag

        // Add to world and remember its ID
        playerId = world.addEntity(player);
    }

    @Override
    public void update(GameData gameData, World world) {

    }
}
