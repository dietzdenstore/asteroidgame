package dietz.core;

import dietz.common.data.GameData;
import dietz.common.data.World;
import dietz.common.data.Entity;
import dietz.common.data.WallCollisionMode;
import dietz.common.data.GameKeys;
import dietz.common.services.IGamePluginService;
import dietz.common.services.IEntityProcessingService;
import dietz.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    private final List<IGamePluginService> gamePlugins;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<String, Node> entityViews = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private Pane gamePane;
    private Scene scene;
    private Label wallModeLabel;

    public Game(
            List<IGamePluginService> gamePlugins,
            List<IEntityProcessingService> entityProcessors,
            List<IPostEntityProcessingService> postProcessors
    ) {
        this.gamePlugins = gamePlugins;
        this.entityProcessors = entityProcessors;
        this.postProcessors = postProcessors;
    }

    public void start(Stage primaryStage) {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        scene = new Scene(gamePane, 1600, 900);

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (!activeKeys.contains(code)) activeKeys.add(code);
            if (code == KeyCode.F) {
                Stage stage = (Stage) scene.getWindow();
                stage.setFullScreen(!stage.isFullScreen());
            } else if (code == KeyCode.TAB) {
                WallCollisionMode current = gameData.getWallMode();
                WallCollisionMode[] modes = WallCollisionMode.values();
                int next = (current.ordinal() + 1) % modes.length;
                gameData.setWallMode(modes[next]);
                wallModeLabel.setText("Wall Mode: " + modes[next]);
            }
        });

        scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));

        wallModeLabel = new Label("Wall Mode: " + gameData.getWallMode());
        wallModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff; -fx-background-color: rgba(197,197,197,0.6);");
        wallModeLabel.setTranslateX(10);
        wallModeLabel.setTranslateY(10);
        gamePane.getChildren().add(wallModeLabel);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dietz Asteroids");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        for (IGamePluginService plugin : gamePlugins) {
            plugin.start(gameData, world);
        }

        resizeArena();
        scene.widthProperty().addListener((obs, o, n) -> resizeArena());
        scene.heightProperty().addListener((obs, o, n) -> resizeArena());

        new AnimationTimer() {
            private long last = System.nanoTime();
            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;
                gameData.setDeltaTime(dt);
                Game.this.updateKeys();
                for (IEntityProcessingService s : entityProcessors) s.process(gameData, world);
                for (IPostEntityProcessingService s : postProcessors) s.process(gameData, world);
                Game.this.syncViews();
            }
        }.start();
    }

    private void updateKeys() {
        GameKeys keys = gameData.getKeys();
        keys.setKey(GameKeys.UP, activeKeys.contains(KeyCode.W));
        keys.setKey(GameKeys.LEFT, activeKeys.contains(KeyCode.A));
        keys.setKey(GameKeys.RIGHT, activeKeys.contains(KeyCode.D));
        keys.setKey(GameKeys.SPACE, activeKeys.contains(KeyCode.SPACE));
        keys.update();
    }

    private void resizeArena() {
        double w = scene.getWidth(), h = scene.getHeight();
        gamePane.setPrefSize(w, h);
        gameData.setDisplayWidth((int) w);
        gameData.setDisplayHeight((int) h);
    }

    private void syncViews() {
        Iterator<Map.Entry<String, Node>> it = entityViews.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Node> e = it.next();
            if (world.getEntity(e.getKey()) == null) {
                gamePane.getChildren().remove(e.getValue());
                it.remove();
            }
        }
        for (Entity e : world.getEntities()) {
            Node view = entityViews.get(e.getID());
            if (view == null) {
                Polygon poly = new Polygon(e.getPolygonCoordinates());
                poly.setRotate(e.getRotation());
                poly.setTranslateX(e.getX());
                poly.setTranslateY(e.getY());
                Color c = e.getBaseColor();
                poly.setFill(c.darker());
                poly.setStroke(c.brighter());
                poly.setStrokeWidth(2);
                if (e.getType().equals("Bullet")) {
                    DropShadow ds = new DropShadow(30, c.brighter());
                    ds.setSpread(0.7);
                    Glow g = new Glow(0.3);
                    g.setInput(ds);
                    Bloom b = new Bloom(0.3);
                    b.setInput(g);
                    poly.setEffect(b);
                    poly.setCache(true);
                    poly.setCacheHint(CacheHint.QUALITY);
                }
                gamePane.getChildren().add(poly);
                entityViews.put(e.getID(), poly);
            } else {
                view.setTranslateX(e.getX());
                view.setTranslateY(e.getY());
                view.setRotate(e.getRotation());
            }
        }
    }
}
