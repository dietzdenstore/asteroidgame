package dietz.asteroid;

import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessing;

public class AsteroidProcessor implements IEntityProcessing {

    @Override
    public void process(GameData gameData, World world) {
        for(Entity entity : world.getEntities(Asteroid.class)) {
            Asteroid asteroid = (Asteroid) entity;

            // Update position
            asteroid.setX(asteroid.getX() + asteroid.getDx() * gameData.getDeltaTime());
            asteroid.setY(asteroid.getY() + asteroid.getDy() * gameData.getDeltaTime());

            // Screen wrapping
            wrapAroundScreen(asteroid, gameData);
        }
    }

    private void wrapAroundScreen(Asteroid asteroid, GameData gameData) {
        double x = asteroid.getX();
        double y = asteroid.getY();
        double radius = asteroid.getRadius();

        if(x < -radius) x = gameData.getDisplayWidth() + radius;
        if(x > gameData.getDisplayWidth() + radius) x = -radius;
        if(y < -radius) y = gameData.getDisplayHeight() + radius;
        if(y > gameData.getDisplayHeight() + radius) y = -radius;

        asteroid.setX(x);
        asteroid.setY(y);
    }
}