package com.sofcprojekt2kunz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        OptimalPath optimalPath = new OptimalPath();
        BorderPane root = new BorderPane();
        root.setCenter(optimalPath);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Obchodní cestující-KUNZ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
