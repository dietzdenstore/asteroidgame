package dietz.player;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {
    private String playerId;

    @Override
    public void start(GameData gameData, World world) {
        Player player = new Player();
        player.setX(gameData.getDisplayWidth()  / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        playerId = world.addEntity(player);
    }

    @Override
    public void update(GameData gameData, World world) {

    }
}
