package dk.sdu.cbse.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private Pane gameWindow = new Pane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        // Create the scene with the game window
        Scene scene = new Scene(gameWindow, 800, 600);  // Example width and height

        // Set up the scene for the window (stage)
        window.setScene(scene);
        window.setTitle("JavaFX Game Window");

        // Show the window
        window.show();
    }
}
