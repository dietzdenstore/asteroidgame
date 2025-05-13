package dietz.asteroid;


import dietz.common.Entity;
import dietz.common.GameData;
import dietz.common.services.IGamePlugin;
import dietz.common.World;

public class AsteroidPlugin implements IGamePlugin {

    @Override
    public void start(GameData gameData, World world) {
        for(int i = 0; i < 4; i++) {
            Asteroid asteroid = new Asteroid(
                    Asteroid.Size.LARGE,
                    Math.random() * gameData.getDisplayWidth(),
                    Math.random() * gameData.getDisplayHeight(),
                    Math.random() * 360
            );
            world.addEntity(asteroid);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for(Entity e : world.getEntities(Asteroid.class)) {
            world.removeEntity(e);
        }
    }

    @Override
    public void update(GameData data, World world) {}
}