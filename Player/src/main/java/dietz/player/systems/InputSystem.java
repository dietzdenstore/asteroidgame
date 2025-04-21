package dietz.player.systems;

import dietz.common.GameKeys;

public class InputSystem {
    public boolean isRotatingLeft(GameKeys keys) {
        return keys.isDown(GameKeys.LEFT);
    }

    public boolean isRotatingRight(GameKeys keys) {
        return keys.isDown(GameKeys.RIGHT);
    }

    public boolean isAccelerating(GameKeys keys) {
        return keys.isDown(GameKeys.UP);
    }

    public boolean isShooting(GameKeys keys) {
        return keys.isDown(GameKeys.SPACE);
    }
}
