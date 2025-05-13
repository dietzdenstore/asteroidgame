package dietz.enemy.systems;

import dietz.common.data.WallCollisionMode;
import dietz.enemy.Enemy;

public class MovementSystem {
    public void applyThrust(Enemy enemy, float dt) {
        double rad = Math.toRadians(enemy.getRotation());
        enemy.setDx(enemy.getDx() + Math.cos(rad) * Enemy.ACCEL * dt);
        enemy.setDy(enemy.getDy() + Math.sin(rad) * Enemy.ACCEL * dt);
        clampVelocity(enemy);
    }

    public void applyDeceleration(Enemy enemy, float dt, double decelFactor) {
        enemy.setDx(enemy.getDx() * Math.pow(decelFactor, dt * 60));
        enemy.setDy(enemy.getDy() * Math.pow(decelFactor, dt * 60));
    }


    private void clampVelocity(Enemy enemy) {
        double speed = Math.hypot(enemy.getDx(), enemy.getDy());
        if (speed > Enemy.MAX_SPEED) {
            double scale = Enemy.MAX_SPEED / speed;
            enemy.setDx(enemy.getDx() * scale);
            enemy.setDy(enemy.getDy() * scale);
        }
    }

    public void moveAndHandleWalls(Enemy enemy, float dt, int w, int h, WallCollisionMode mode) {
        double newX = enemy.getX() + enemy.getDx() * dt;
        double newY = enemy.getY() + enemy.getDy() * dt;
        double dx = enemy.getDx();
        double dy = enemy.getDy();

        switch (mode) {
            case WRAP:
                newX = wrap(newX, w);
                newY = wrap(newY, h);
                break;

            case BOUNCE:
                if (newX < 0 || newX > w) {
                    dx *= -1;
                    newX = Math.max(0, Math.min(newX, w));
                }
                if (newY < 0 || newY > h) {
                    dy *= -1;
                    newY = Math.max(0, Math.min(newY, h));
                }
                break;

            case STOP:
                if ((newX < 0 && dx < 0) || (newX > w && dx > 0)) dx = 0;
                if ((newY < 0 && dy < 0) || (newY > h && dy > 0)) dy = 0;
                newX = Math.max(0, Math.min(newX, w));
                newY = Math.max(0, Math.min(newY, h));
                break;
        }

        enemy.setX(newX);
        enemy.setY(newY);
        enemy.setDx(dx);
        enemy.setDy(dy);
    }


    private double wrap(double val, double max) {
        if (val < 0) return val + max;
        if (val > max) return val - max;
        return val;
    }
}
