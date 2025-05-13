package dietz.common.services;

import dietz.common.GameData;
import dietz.common.World;

public interface IGamePlugin {
    void start(GameData gameData, World world);

    void update(GameData gameData, World world);

    void stop(GameData gameData, World world);
}
