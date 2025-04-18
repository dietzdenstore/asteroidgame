package dietz.player;

import dietz.common.*;

public class PlayerPlugin implements GamePlugin {
    private String playerId;

    @Override
    public void start(GameData data, World world) {
        // spawn at center
        Player p = new Player();
        p.setX(data.getDisplayWidth()/2.0);
        p.setY(data.getDisplayHeight()/2.0);
        playerId = world.addEntity(p);
    }

    @Override
    public void update(GameData data, World world) {
        float dt = data.getDeltaTime();
        Player p = (Player) world.getEntity(playerId);
        GameKeys keys = data.getKeys();

        // rotation
        if (keys.isDown(GameKeys.LEFT))  p.setRotation(p.getRotation() - Player.ROT_SPEED * dt);
        if (keys.isDown(GameKeys.RIGHT)) p.setRotation(p.getRotation() + Player.ROT_SPEED * dt);

        // thrust
        if (keys.isDown(GameKeys.UP)) {
            double rad = Math.toRadians(p.getRotation());
            p.setDx(p.getDx() + Math.cos(rad)*Player.ACCEL*dt);
            p.setDy(p.getDy() + Math.sin(rad)*Player.ACCEL*dt);
            // clamp
            double speed = Math.hypot(p.getDx(), p.getDy());
            if (speed > Player.MAX_SPEED) {
                double scale = Player.MAX_SPEED / speed;
                p.setDx(p.getDx()*scale);
                p.setDy(p.getDy()*scale);
            }
        }

        // move & wrap
        double nx = wrap(p.getX() + p.getDx()*dt, data.getDisplayWidth());
        double ny = wrap(p.getY() + p.getDy()*dt, data.getDisplayHeight());
        p.setX(nx);
        p.setY(ny);

        // bullets: spawn on press
        if (keys.isPressed(GameKeys.SPACE)) {
            Bullet b = new Bullet(p.getX(), p.getY(), p.getRotation());
            world.addEntity(b);
        }

        // update all bullets
        for (Entity e : world.getEntities(Bullet.class)) {
            ((Bullet)e).update(dt, data.getDisplayWidth(), data.getDisplayHeight());
        }

        // finally, update key-history
        data.getKeys().update();
    }

    private double wrap(double v, double max) {
        if (v < 0)   return v + max;
        if (v > max) return v - max;
        return v;
    }
}
