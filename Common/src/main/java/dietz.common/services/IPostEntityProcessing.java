package dietz.common.services;

import dietz.common.GameData;
import dietz.common.World;

public interface IPostEntityProcessing {
    void process(GameData gameData, World world);
}
