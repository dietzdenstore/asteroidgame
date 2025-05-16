package dietz.common.data;

public class GameData {

    private int displayWidth  = 1920;
    private int displayHeight = 900;
    private final GameKeys keys = new GameKeys();

    private float deltaTime;


    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDeltaTime(float dt) {
        this.deltaTime = dt;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    private WallCollisionMode wallMode = WallCollisionMode.BOUNCE;

    public WallCollisionMode getWallMode() {
        return wallMode;
    }

    public void setWallMode(WallCollisionMode mode) {
        this.wallMode = mode;
    }


}