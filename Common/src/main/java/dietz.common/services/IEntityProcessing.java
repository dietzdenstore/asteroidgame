package dietz.common.services;

import dietz.common.GameData;
import dietz.common.World;

public interface IEntityProcessing {
    void process(GameData gameData, World world);
}
