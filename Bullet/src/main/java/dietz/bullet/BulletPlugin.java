package dietz.bullet;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IGamePluginService;

public class BulletPlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) { }
    @Override
    public void stop(GameData gameData, World world) {
        world.getEntities(Bullet.class).forEach(world::removeEntity);
    }
}
