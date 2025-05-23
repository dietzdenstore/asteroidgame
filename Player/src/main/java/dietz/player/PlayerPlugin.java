package dietz.player;

import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePluginService;

public class PlayerPlugin implements IGamePluginService {
    private Entity playerId;

    @Override
    public void start(GameData gameData, World world) {
     playerId = PlayerFactory.spawnPlayer(gameData, world);
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(playerId);
    }
}
