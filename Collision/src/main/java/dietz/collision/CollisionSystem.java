package dietz.collision;

import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.components.Health;
import dietz.common.data.*;
import dietz.common.services.IPostEntityProcessingService;
import dietz.common.util.ServiceLocator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CollisionSystem implements IPostEntityProcessingService {

    private final IAsteroidSplitter asteroidSplitter;
    private final List<Entity> toSplit = new ArrayList<>();

    HttpClient client = HttpClient.newHttpClient();

    public CollisionSystem() {
        List<IAsteroidSplitter> splitters = ServiceLocator.INSTANCE.locateAll(IAsteroidSplitter.class);
        if (!splitters.isEmpty()) {
            this.asteroidSplitter = splitters.get(0);
        } else {
            this.asteroidSplitter = (original, world) -> { };
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        toSplit.clear();

        List<Entity> entities = new ArrayList<>(world.getEntities());

        // O(N²) pair-wise test
        for (int i = 0; i < entities.size(); i++) {
            Entity e1 = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity e2 = entities.get(j);
                if (collides(e1, e2)) {
                    handleCollision(e1, e2, world);
                }
            }
            for (Entity asteroid : toSplit) {
                asteroidSplitter.split(asteroid, world);
            }
        }
    }

    private void handleCollision(Entity e1, Entity e2, World world) {
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
                        incrementScore(1);
                        break;
                    case "Player":
                        damage(e2, world);
                        world.removeEntity(e1);
                        break;
                    case "Enemy":
                        damage(e2, world);
                        world.removeEntity(e1);
                        incrementScore(3);
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
                        incrementScore(1);
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
        asteroidSplitter.split(asteroid, world);
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
        double m1 = e1.getRadius()*e1.getRadius();
        double m2 = e2.getRadius()*e2.getRadius();

        double dx = e1.getX() - e2.getX();
        double dy = e1.getY() - e2.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);
        if (dist < 0.000001) return;
        double nx = dx/dist;
        double ny = dy/dist;


        double dvx = e1.getDx() - e2.getDx();
        double dvy = e1.getDy() - e2.getDy();
        double vn = dvx*nx + dvy*ny;
        if (vn >= 0) return;  // separating already

        double j = (2 * vn) / (m1 + m2);

        e1.setDx(e1.getDx() - j * m2 * nx);
        e1.setDy(e1.getDy() - j * m2 * ny);
        e2.setDx(e2.getDx() + j * m1 * nx);
        e2.setDy(e2.getDy() + j * m1 * ny);
    }
    private void incrementScore(int i) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/score/add/" + i))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenAccept(r -> System.out.println("score +"+i+" → "+r.statusCode()))
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }
}
