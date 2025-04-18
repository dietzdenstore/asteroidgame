package dietz.core;

import dietz.common.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.*;

public class App extends Application {
    private final GameData gameData = new GameData();
    private final World world       = new World();
    private final List<GamePlugin> plugins = new ArrayList<>();
    private final Map<String,Node> entityViews = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Asteroids Player Demo");
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        // -- wire keys --
        Scene scene = new Scene(root);
        scene.setOnKeyPressed  (e -> handleKey(e, true));
        scene.setOnKeyReleased (e -> handleKey(e, false));
        primaryStage.setScene(scene);
        primaryStage.show();

        // -- load all plugins via ServiceLoader --
        ServiceLoader<GamePlugin> loader = ServiceLoader.load(GamePlugin.class);
        loader.forEach(p -> {
            plugins.add(p);
            p.start(gameData, world);
        });

        // -- game loop --
        new AnimationTimer() {
            private long last = System.nanoTime();
            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;
                gameData.setDeltaTime(dt);

                // update logic
                for (GamePlugin p : plugins) {
                    p.update(gameData, world);
                }
                // render
                syncViews(root);
            }
        }.start();
    }

    private void handleKey(KeyEvent e, boolean down) {
        KeyCode code = e.getCode();
        if (code == KeyCode.UP) {
            gameData.getKeys().setKey(GameKeys.UP, down);
        }
        if (code == KeyCode.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, down);
        }
        if (code == KeyCode.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, down);
        }
        if (code == KeyCode.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, down);
        }
    }

    private void syncViews(Pane root) {
        // 1) remove views for entities no longer in world
        var toRemove = new ArrayList<String>();
        for (var id : entityViews.keySet()) {
            if (world.getEntity(id) == null) {
                root.getChildren().remove(entityViews.get(id));
                toRemove.add(id);
            }
        }
        toRemove.forEach(entityViews::remove);

        // 2) for each entity, ensure a Node exists & update its transform
        for (Entity e : world.getEntities()) {
            String id = e.getID();
            Node view = entityViews.get(id);
            if (view == null) {
                // assume polygon-shaped ships/bullets
                Polygon poly = new Polygon(e.getPolygonCoordinates());
                poly.setTranslateX(e.getX());
                poly.setTranslateY(e.getY());
                poly.setRotate(e.getRotation());
                root.getChildren().add(poly);
                entityViews.put(id, poly);
            } else {
                view.setTranslateX(e.getX());
                view.setTranslateY(e.getY());
                view.setRotate(e.getRotation());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
