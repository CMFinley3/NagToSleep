package com.imfnly.nag;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String APPLICATION_NAME = "Nag To Sleep";
    public static final String ICON_IMAGE_FILe = "zzz.png";
    public static final Image ICON_IMAGE = Toolkit.getDefaultToolkit().getImage(ICON_IMAGE_FILe);
    public static final TrayIcon TRAY_ICON = new TrayIcon(ICON_IMAGE, APPLICATION_NAME);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);
        primaryStage.setTitle(APPLICATION_NAME);
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setOnCloseRequest(event -> {
            primaryStage.hide();
            event.consume();
        });
        primaryStage.show();

        TrayMenu menu = new TrayMenu();
        menu.setClickAction(() -> Platform.runLater(() -> toggleVisibility(primaryStage)));
        menu.addAction("Show", () -> Platform.runLater(() -> primaryStage.show()));
        menu.addAction("Hide", () -> Platform.runLater(() -> primaryStage.hide()));
        menu.addAction("Exit", () -> System.exit(0));

        LocalTime nagTime = PropertiesWrapper.getTime(ETimeKeys.NAG);
        LocalTime sleepTime = PropertiesWrapper.getTime(ETimeKeys.SLEEP);
        LocalTime wakeTime = PropertiesWrapper.getTime(ETimeKeys.WAKE);
        NagTimer timer = new NagTimer(nagTime, sleepTime, wakeTime);
        timer.start();
    }

    public static void toggleVisibility(Stage stage) {
        if (stage.isShowing()) {
            stage.hide();
        } else {
            stage.show();
        }
    }

    public static void sendNotification(String title, String message) {
        TRAY_ICON.displayMessage(title, message, MessageType.INFO);
    }
}
