package dietz.common.data;

public class GameKeys {

    private static boolean[] keys;
    private static boolean[] pkeys;

    private static final int keyAmount = 5;
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int SPACE = 3;
    public static final int DOWN = 4;

    public GameKeys() {
        keys = new boolean[keyAmount];
        pkeys = new boolean[keyAmount];
    }

    public void update() {
        for (int i = 0; i < keyAmount; i++) {
            pkeys[i] = keys[i];
        }
    }

    public void setKey(int k, boolean b) {
        keys[k] = b;
    }

    public boolean isDown(int k) {
        return keys[k];
    }


}