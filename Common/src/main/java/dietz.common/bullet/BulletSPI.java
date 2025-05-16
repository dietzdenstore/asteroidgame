package dietz.common.bullet;

import dietz.common.data.Entity;
import dietz.common.data.GameData;

public interface BulletSPI {
    Entity createBullet(Entity shooter, GameData gameData);
}
