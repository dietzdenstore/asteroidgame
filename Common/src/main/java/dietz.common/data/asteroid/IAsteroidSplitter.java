package dietz.common.data.asteroid;

import dietz.common.data.Entity;
import dietz.common.data.World;

public interface IAsteroidSplitter {
    void createSplitAsteroid(Entity original, World world);
}
