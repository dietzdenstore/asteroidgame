package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;

public interface IGamePluginService {
    void start(GameData gameData, World world);

    void update(GameData gameData, World world);

}
