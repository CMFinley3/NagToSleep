package com.imfnly.nag;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
        });
        primaryStage.show();

        TrayMenu menu = new TrayMenu("zzz.png");
        menu.setClickAction(() -> Platform.runLater(() -> toggleVisibility(primaryStage)));
        menu.addAction("Show", () -> Platform.runLater(() -> primaryStage.show()));
        menu.addAction("Hide", () -> Platform.runLater(() -> primaryStage.hide()));
        menu.addAction("Exit", () -> System.exit(0));
    }

    public static void toggleVisibility(Stage stage) {
        if (stage.isShowing()) {
            stage.hide();
        } else {
            stage.show();
        }
    }
}
