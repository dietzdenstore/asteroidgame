package dietz.collision;

import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.data.*;
import dietz.common.services.IPostEntityProcessing;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CollisionPlugin implements IPostEntityProcessing {

    private final IAsteroidSplitter asteroidSplitter;
    private static final float EPS = 0.001f;

    public CollisionPlugin() {
        asteroidSplitter = ServiceLoader.load(IAsteroidSplitter.class)
                .findFirst()
                .orElse((original, world) -> {});
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
                        elasticBounce(e1, e2);
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

    private void elasticBounce(Entity e1, Entity e2) {
        // approximate mass ∝ r²
        double m1 = e1.getRadius()*e1.getRadius();
        double m2 = e2.getRadius()*e2.getRadius();

        // compute normalized collision normal n = (pos1-pos2)/|pos1-pos2|
        double dx = e1.getX() - e2.getX();
        double dy = e1.getY() - e2.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist < 0.000001) return;
        double nx = dx/dist;
        double ny = dy/dist;

        // relative velocity along n
        double dvx = e1.getDx() - e2.getDx();
        double dvy = e1.getDy() - e2.getDy();
        double vn = dvx*nx + dvy*ny;
        if (vn >= 0) return;  // separating already

        // impulse scalar for perfectly elastic collision
        double j = (2 * vn) / (m1 + m2);

        // apply impulse: v' = v – (j * m_other) * n
        e1.setDx(e1.getDx() - j * m2 * nx);
        e1.setDy(e1.getDy() - j * m2 * ny);
        e2.setDx(e2.getDx() + j * m1 * nx);
        e2.setDy(e2.getDy() + j * m1 * ny);
    }
}
