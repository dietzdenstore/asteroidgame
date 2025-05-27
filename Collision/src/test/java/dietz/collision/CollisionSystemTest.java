package dietz.collision;

import dietz.common.asteroid.IAsteroidSplitter;
import dietz.common.components.Health;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollisionSystemTest {

    /**
     * Helper to inject a mock IAsteroidSplitter into a CollisionSystem instance,
     * bypassing the private final field via reflection.
     */
    private void injectSplitter(CollisionSystem system, IAsteroidSplitter splitter) throws Exception {
        Field f = CollisionSystem.class.getDeclaredField("asteroidSplitter");
        f.setAccessible(true);
        f.set(system, splitter);
    }

    @Test
    void noCollisionFarApart() {
        World world = new World();
        CollisionSystem cs = new CollisionSystem();

        Entity a1 = new Entity();
        a1.setType("Asteroid");
        a1.setRadius(5f);
        a1.setX(0); a1.setY(0);

        Entity a2 = new Entity();
        a2.setType("Asteroid");
        a2.setRadius(5f);
        a2.setX(100); a2.setY(100);

        world.addEntity(a1);
        world.addEntity(a2);

        cs.process(new GameData(), world);

        assertEquals(2, world.getEntityCount("Asteroid"),
                "Asteroids far apart should not collide");
    }

    @Test
    void asteroidsCollision() {
        World world = new World();
        CollisionSystem cs = new CollisionSystem();

        Entity e1 = new Entity();
        e1.setType("Asteroid");
        e1.setRadius(5f);
        e1.setX(0); e1.setY(0);
        e1.setDx(1); e1.setDy(0);

        Entity e2 = new Entity();
        e2.setType("Asteroid");
        e2.setRadius(5f);
        e2.setX(9); e2.setY(0);
        e2.setDx(-1); e2.setDy(0);

        world.addEntity(e1);
        world.addEntity(e2);

        cs.process(new GameData(), world);

        assertEquals(-1.0, e1.getDx(), 0.000006, "e1 should reverse direction");
        assertEquals(+1.0, e2.getDx(), 0.000006, "e2 should reverse direction");
    }

    @Test
    void bulletAsteroidCollision() throws Exception {
        World world = new World();
        GameData gd = new GameData();
        CollisionSystem cs = new CollisionSystem();

        // prepare a mock splitter
        IAsteroidSplitter mockSplitter = mock(IAsteroidSplitter.class);
        injectSplitter(cs, mockSplitter);

        Entity bullet = new Entity();
        bullet.setType("Bullet");
        bullet.setRadius(1f);
        bullet.setX(0); bullet.setY(0);

        Entity asteroid = new Entity();
        asteroid.setType("Asteroid");
        asteroid.setRadius(5f);
        asteroid.setX(0); asteroid.setY(0);

        world.addEntity(bullet);
        world.addEntity(asteroid);

        cs.process(gd, world);

        // both entities gone
        assertEquals(0, world.getEntityCount("Bullet"),
                "Bullet should be removed on collision with asteroid");
        assertEquals(0, world.getEntityCount("Asteroid"),
                "Asteroid should be removed when hit by a bullet");

        // splitter called exactly once on the original asteroid
        verify(mockSplitter, times(1)).split(asteroid, world);
    }

    @Test
    void removePlayerWithoutHealth() {
        World world = new World();
        CollisionSystem cs = new CollisionSystem();
        GameData gd = new GameData();

        Entity player = new Entity();
        player.setType("Player");
        player.setRadius(5f);
        player.setX(0); player.setY(0);

        Entity bullet = new Entity();
        bullet.setType("Bullet");
        bullet.setRadius(1f);
        bullet.setX(0); bullet.setY(0);

        world.addEntity(player);
        world.addEntity(bullet);

        cs.process(gd, world);

        // player has no Health ⇒ fallback removal
        assertEquals(0, world.getEntityCount("Player"),
                "Player without Health should be removed when hit by bullet");
        assertEquals(0, world.getEntityCount("Bullet"),
                "Bullet should always be removed on impact");
    }

    @Test
    void decrementHealthOnHit() {
        World world = new World();
        CollisionSystem cs = new CollisionSystem();
        GameData gd = new GameData();

        Entity player = new Entity();
        player.setType("Player");
        player.setRadius(5f);
        player.setX(0); player.setY(0);
        player.addComponent(new Health(2));  // start with 2 HP

        Entity bullet = new Entity();
        bullet.setType("Bullet");
        bullet.setRadius(1f);
        bullet.setX(0); bullet.setY(0);

        world.addEntity(player);
        world.addEntity(bullet);

        cs.process(gd, world);

        // bullet removed
        assertEquals(0, world.getEntityCount("Bullet"),
                "Bullet should be removed on hitting player");

        // player still present with HP = 1
        assertEquals(1, world.getEntityCount("Player"),
                "Player should remain alive with Health > 0");
        Health hp = player.getComponent(Health.class);
        assertNotNull(hp);
        assertEquals(1.0, hp.getHealth(), 1e-6,
                "Health should decrement by exactly 1");
    }
}
