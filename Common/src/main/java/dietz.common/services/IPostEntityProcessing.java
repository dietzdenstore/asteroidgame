package dietz.common.services;

import dietz.common.data.GameData;
import dietz.common.data.World;

public interface IPostEntityProcessing {
    void process(GameData gameData, World world);
}
