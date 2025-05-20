package dietz.collision;

import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.data.*;
import dietz.common.services.IPostEntityProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionPlugin implements IPostEntityProcessing {

    private final IAsteroidSplitter asteroidSplitter;

    public CollisionPlugin() {
        asteroidSplitter = ServiceLoader.load(IAsteroidSplitter.class)
                .findFirst()
                .orElse((original, world) -> {/* no-op */});
    }

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());

        // O(N²) pair-wise test
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (collides(e1, e2)) {
                    handleCollisionPair(e1, e2, world);
                }
            }
        }
    }

    private void handleCollisionPair(Entity e1, Entity e2, World world) {
        String t1 = e1.getType();
        String t2 = e2.getType();

        switch (t1) {
            case "Player":
                switch (t2) {
                    case "Asteroid":
                        world.removeEntity(e1);
                        break;
                    case "Bullet":
                        damage(e1, world);
                        world.removeEntity(e2);
                        break;
                    case "Enemy":
                        world.removeEntity(e1);
                        world.removeEntity(e2);
                        break;
                }
                break;

            case "Enemy":
                switch (t2) {
                    case "Asteroid":
                        world.removeEntity(e1);
                        break;
                    case "Bullet":
                        damage(e1, world);
                        world.removeEntity(e2);
                        break;
                    case "Player":
                        world.removeEntity(e1);
                        world.removeEntity(e2);
                        break;
                }
                break;

            case "Bullet":
                switch (t2) {
                    case "Asteroid":
                        world.removeEntity(e1);
                        splitAndRemoveAsteroid(e2, world);
                        break;
                    case "Player":
                        damage(e2, world);
                        world.removeEntity(e1);
                        break;
                    case "Enemy":
                        damage(e2, world);
                        world.removeEntity(e1);
                        break;
                }
                break;

            case "Asteroid":
                switch (t2) {
                    case "Player":
                        world.removeEntity(e2);
                        break;
                    case "Enemy":
                        world.removeEntity(e2);
                        break;
                    case "Bullet":
                        world.removeEntity(e2);
                        splitAndRemoveAsteroid(e1, world);
                        break;
                    case "Asteroid":
                        splitAndRemoveAsteroid(e1, world);
                        break;
                }
                break;
        }
    }

    private boolean collides(Entity a, Entity b) {
        float dx = (float) (a.getX() - b.getX());
        float dy = (float) (a.getY() - b.getY());
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return dist < a.getRadius() + b.getRadius();
    }


    private void splitAndRemoveAsteroid(Entity asteroid, World world) {
        asteroidSplitter.createSplitAsteroid(asteroid, world);
        world.removeEntity(asteroid);
    }


    private void damage(Entity target, World world) {
        Health hp = target.getComponent(Health.class);
        if (hp != null) {
            hp.setHealth(hp.getHealth() - 1);
            if (hp.getHealth() <= 0) {
                world.removeEntity(target);
            }
        } else {
            world.removeEntity(target);    // fallback if no HP component
        }
    }
}
