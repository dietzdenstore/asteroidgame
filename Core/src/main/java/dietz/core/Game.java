// Core/src/main/java/dietz/core/Game.java
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
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    // new: HTTP client and score Text
    private HttpClient httpClient;
    private Text scoreText;

    private static final String SCORE_SERVICE_URL = "http://localhost:8080";

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
        // 1) init HTTP client & reset remote score
        httpClient = HttpClient.newHttpClient();
        resetScore();

        // 2) build scene
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: black;");
        scene = new Scene(gamePane, 1600, 900);

        scene.setOnKeyPressed(this::handleKeyPress);
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));

        // 3) HUD: wall mode label
        wallModeLabel = new Label("Wall Mode: " + gameData.getWallMode());
        wallModeLabel.setStyle("-fx-font-size:14px; -fx-text-fill:#fff; -fx-background-color:rgba(197,197,197,0.6);");
        wallModeLabel.setTranslateX(10);
        wallModeLabel.setTranslateY(10);
        gamePane.getChildren().add(wallModeLabel);

        // 4) HUD: live score
        scoreText = new Text(10, 40, "Score: 0");
        scoreText.setFill(Color.WHITE);
        gamePane.getChildren().add(scoreText);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dietz Asteroids");
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

        // 5) start plugins
        for (IGamePluginService p : gamePlugins) {
            p.start(gameData, world);
        }

        // 6) resize logic
        resizeArena();
        scene.widthProperty().addListener((obs, o, n) -> resizeArena());
        scene.heightProperty().addListener((obs, o, n) -> resizeArena());

        // 7) game loop with non-blocking score polling
        new AnimationTimer() {
            private long last = System.nanoTime();

            @Override
            public void handle(long now) {
                float dt = (now - last) / 1_000_000_000f;
                last = now;

                gameData.setDeltaTime(dt);
                updateKeys();

                for (IEntityProcessingService s : entityProcessors) {
                    s.process(gameData, world);
                }
                for (IPostEntityProcessingService s : postProcessors) {
                    s.process(gameData, world);
                }

                // non-blocking fetch and update of score
                pollScore();

                syncViews();
            }
        }.start();
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();
        if (!activeKeys.contains(code)) activeKeys.add(code);

        if (code == KeyCode.F) {
            Stage st = (Stage) scene.getWindow();
            st.setFullScreen(!st.isFullScreen());
        } else if (code == KeyCode.TAB) {
            WallCollisionMode current = gameData.getWallMode();
            WallCollisionMode[] modes = WallCollisionMode.values();
            int next = (current.ordinal() + 1) % modes.length;
            gameData.setWallMode(modes[next]);
            wallModeLabel.setText("Wall Mode: " + modes[next]);
        }
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

    /** Asynchronously resets score to 0 on startup. */
    private void resetScore() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SCORE_SERVICE_URL + "/score/set/0"))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .exceptionally(ex -> null);
    }

    /** Asynchronously GETs the current score and updates the Text node. */
    private void pollScore() {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(SCORE_SERVICE_URL + "/score/get"))
                .GET()
                .build();
        httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body ->
                        Platform.runLater(() -> scoreText.setText("Score: " + body))
                )
                .exceptionally(ex -> null);
    }
}
