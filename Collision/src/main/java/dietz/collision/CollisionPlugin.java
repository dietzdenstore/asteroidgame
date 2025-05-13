package dietz.collision;

import dietz.asteroid.Asteroid;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.data.asteroid.IAsteroidSplitter;
import dietz.bullet.Bullet;
import dietz.common.services.IGamePlugin;
import dietz.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionPlugin implements IGamePlugin {        // ← changed

    private final IAsteroidSplitter splitter =
            ServiceLoader.load(IAsteroidSplitter.class)
                    .findFirst()
                    .orElse((e, w) -> {});

    @Override public void start(GameData data, World world) {}

    @Override
    public void update(GameData data, World world) {
        List<Entity> ents = new ArrayList<>(world.getEntities());

        for (int i = 0; i < ents.size(); i++) {
            Entity a = ents.get(i);
            for (int j = i + 1; j < ents.size(); j++) {
                Entity b = ents.get(j);
                if (!collides(a, b)) continue;

                if (a instanceof Bullet && b instanceof Asteroid) {
                    world.removeEntity(a);
                    splitter.createSplitAsteroid(b, world);
                    world.removeEntity(b);
                } else if (b instanceof Bullet && a instanceof Asteroid) {
                    world.removeEntity(b);
                    splitter.createSplitAsteroid(a, world);
                    world.removeEntity(a);
                } else if (a instanceof Bullet && b instanceof Enemy) {
                    world.removeEntity(a);
                    world.removeEntity(b);
                } else if (b instanceof Bullet && a instanceof Enemy) {
                    world.removeEntity(b);
                    world.removeEntity(a);
                }
            }
        }
    }

    private boolean collides(Entity e1, Entity e2) {
        double dx = e1.getX() - e2.getX();
        double dy = e1.getY() - e2.getY();
        return Math.hypot(dx, dy) < e1.getRadius() + e2.getRadius();
    }
}
