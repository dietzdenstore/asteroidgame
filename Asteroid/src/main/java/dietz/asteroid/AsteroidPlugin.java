package dietz.asteroid;


import dietz.common.data.GameData;
import dietz.common.services.IGamePlugin;
import dietz.common.data.World;

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
    public void update(GameData data, World world) {}
}