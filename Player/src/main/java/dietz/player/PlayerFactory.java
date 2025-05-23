package dietz.player;

import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;

public class PlayerFactory {

public static Entity spawnPlayer(GameData gameData, World world){
    Player player = new Player();
        player.setX(gameData.getDisplayWidth()  / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        world.addEntity(player);
        return player;
    }
}
