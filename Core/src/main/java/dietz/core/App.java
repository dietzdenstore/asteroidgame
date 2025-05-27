package dietz.core;

import dietz.common.data.*;
import dietz.common.services.IGamePluginService;
import dietz.common.services.IPostEntityProcessingService;
import dietz.common.util.ServiceLocator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import dietz.common.services.IEntityProcessingService;

import java.util.*;

public class App extends Application {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final List<IGamePluginService> plugins = new ArrayList<>();
    private final Map<String, Node> entityViews = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    private Pane gamePane;
    private Scene scene;

    private Label wallModeLabel;

    private final List<IEntityProcessingService> processors     = new ArrayList<>();
    private final List<IPostEntityProcessingService> postProcessors = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        scene = new Scene(gamePane, 1600, 900);

        setupKeyHandling();
        setupLabels();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dietz Asteroids");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        loadPlugins();
        loadProcessors();
        resizeArena();

        // Handle resize
        scene.widthProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                resizeArena();
            }
        });

        scene.heightProperty().addListener(new javafx.beans.value.ChangeListener<Number>() {
            public void changed(javafx.beans.value.ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                resizeArena();
            }
        });

        new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;

                gameData.setDeltaTime(dt);
                updateKeys();

                for (IEntityProcessingService s : processors) {
                    s.process(gameData, world);
                }
                for (IPostEntityProcessingService s : postProcessors) {
                    s.process(gameData, world);
                }

                syncViews();
            }
        }.start();
    }

    private void setupKeyHandling() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (!activeKeys.contains(code)) {
                    activeKeys.add(code);
                }
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
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                activeKeys.remove(event.getCode());
            }
        });
    }

    private void updateKeys() {
        GameKeys keys = gameData.getKeys();
        keys.setKey(GameKeys.UP,     activeKeys.contains(KeyCode.W));
        keys.setKey(GameKeys.LEFT,   activeKeys.contains(KeyCode.A));
        keys.setKey(GameKeys.RIGHT,  activeKeys.contains(KeyCode.D));
        keys.setKey(GameKeys.SPACE,  activeKeys.contains(KeyCode.SPACE));
        keys.update();
    }

    private void setupLabels() {
        wallModeLabel = new Label("Wall Mode: " + gameData.getWallMode());
        wallModeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffffff; -fx-background-color: rgba(197,197,197,0.6);");
        wallModeLabel.setTranslateX(10);
        wallModeLabel.setTranslateY(10);
        gamePane.getChildren().add(wallModeLabel);
    }

    private <T> Collection<T> locateAll(Class<T> service) {
        return ServiceLocator.INSTANCE.locateAll(service);
    }

    private void loadPlugins() {
        locateAll(IGamePluginService.class).forEach(p -> {
            plugins.add(p);
            p.start(gameData, world);
        });
    }

    private void loadProcessors() {
        processors.addAll(locateAll(IEntityProcessingService.class));
        postProcessors.addAll(locateAll(IPostEntityProcessingService.class));
    }

    private void resizeArena() {
        double width = scene.getWidth();
        double height = scene.getHeight();
        gamePane.setPrefSize(width, height);
        gameData.setDisplayWidth((int) width);
        gameData.setDisplayHeight((int) height);
    }

    private void syncViews() {
        Iterator<Map.Entry<String, Node>> iterator = entityViews.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Node> entry = iterator.next();
            if (world.getEntity(entry.getKey()) == null) {
                gamePane.getChildren().remove(entry.getValue());
                iterator.remove();
            }
        }
        for (Entity e : world.getEntities()) {
            Node view = entityViews.get(e.getID());
            if (view == null) {
                Polygon poly = new Polygon(e.getPolygonCoordinates());
                poly.setRotate(e.getRotation());
                poly.setTranslateX(e.getX());
                poly.setTranslateY(e.getY());

                Color entityColor = e.getBaseColor();

                poly.setFill(entityColor.darker());
                poly.setStroke(entityColor.brighter());
                poly.setStrokeWidth(2);

                //Bullet glow
                if(e.getType().equals("Bullet")) {
                    DropShadow ds = new DropShadow(30, entityColor.brighter());
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

    public static void main(String[] args) {
        launch(args);
    }
}
