package dietz.common;

public interface GamePlugin {
    void start(GameData gameData, World world);

    void update(GameData gameData, World world);
}
