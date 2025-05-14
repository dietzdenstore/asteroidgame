package dietz.collision;

import dietz.common.data.asteroid.IAsteroidSplitter;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.Health;
import dietz.common.data.World;
import dietz.common.services.IGamePlugin;
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

    /* ---------------------------------------------------------- *
     *  Main hook called by the game loop after movement systems  *
     * ---------------------------------------------------------- */
    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());

        /*   O(N²) pair-wise test — fine for the small entity counts here   */
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

    /* ---------------------------------------------------------- *
     *  Per-pair dispatch based on the two type strings            *
     * ---------------------------------------------------------- */
    private void handleCollisionPair(Entity e1, Entity e2, World world) {
        String t1 = e1.getType();
        String t2 = e2.getType();

        /* ---- Player <-> Asteroid ------------------------------------ */
        if (t1.equals("Player") && t2.equals("Asteroid")) {
            world.removeEntity(e1);                 // player dies
            return;
        }
        if (t1.equals("Asteroid") && t2.equals("Player")) {
            world.removeEntity(e2);
            return;
        }

        /* ---- Enemy  <-> Asteroid ------------------------------------ */
        if (t1.equals("Enemy") && t2.equals("Asteroid")) {
            world.removeEntity(e1);
            return;
        }
        if (t1.equals("Asteroid") && t2.equals("Enemy")) {
            world.removeEntity(e2);
            return;
        }

        /* ---- Bullet <-> Asteroid  (split) --------------------------- */
        if (t1.equals("Bullet") && t2.equals("Asteroid")) {
            world.removeEntity(e1);                         // bullet
            splitAndRemoveAsteroid(e2, world);             // asteroid
            return;
        }
        if (t1.equals("Asteroid") && t2.equals("Bullet")) {
            world.removeEntity(e2);
            splitAndRemoveAsteroid(e1, world);
            return;
        }

        /* ---- Bullet <-> Player / Enemy (damage) --------------------- */
        if (t1.equals("Bullet") && t2.equals("Player")) {
            damage(e2, world);
            world.removeEntity(e1);     // always delete the bullet
            return;
        }
        if (t1.equals("Player") && t2.equals("Bullet")) {
            damage(e1, world);
            world.removeEntity(e2);
            return;
        }
        if (t1.equals("Bullet") && t2.equals("Enemy")) {
            damage(e2, world);
            world.removeEntity(e1);
            return;
        }
        if (t1.equals("Enemy") && t2.equals("Bullet")) {
            damage(e1, world);
            world.removeEntity(e2);
            return;
        }

        /* ---- Enemy <-> Player (mutual destruction) ------------------ */
        if (t1.equals("Enemy") && t2.equals("Player")
                || t1.equals("Player") && t2.equals("Enemy")) {
            world.removeEntity(e1);
            world.removeEntity(e2);
        }
    }

    /* ---------------------------------------------------------- *
     *  Helpers                                                   *
     * ---------------------------------------------------------- */

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
