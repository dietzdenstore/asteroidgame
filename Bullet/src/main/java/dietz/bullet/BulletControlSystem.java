package dietz.bullet;

import java.util.ArrayList;
import java.util.List;

import dietz.common.bullet.BulletSPI;
import dietz.common.data.Entity;
import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.services.IEntityProcessingService;
import javafx.scene.paint.Color;

public class BulletControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {
        float dt = gameData.getDeltaTime();
        List<Entity> toRemove = new ArrayList<>();
        for (Entity e : world.getEntities(Bullet.class)) {
            Bullet b = (Bullet)e;

            b.setX(b.getX() + b.getDx() * dt);
            b.setY(b.getY() + b.getDy() * dt);
        }
        toRemove.forEach(world::removeEntity);
    }
}
